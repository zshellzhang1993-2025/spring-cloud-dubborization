package cc.zshell.eureka.consumer.api;

import cc.zshell.eureka.common.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.asm.*;
import org.springframework.asm.Type;
import org.springframework.beans.BeansException;
import org.springframework.cglib.proxy.*;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.*;
import java.util.Arrays;

/**
 * 通过动态代理生成实际的 rest consumer
 */
public class HttpProxyGenerator implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(HttpProxyGenerator.class);

    private static RestTemplate rpcRestClient;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        rpcRestClient = applicationContext.getBean("rpcRestClient", RestTemplate.class);
    }

    @SuppressWarnings("unchecked")
    public static <T> T generate(Class<T> actualConsumer, final String serviceNamespace) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(actualConsumer);

        enhancer.setCallback(new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                StringBuilder serviceUrlBuilder = new StringBuilder(100);

                String servicePath = method.getName();
                String[] paramNames = getMethodParameterNamesByAsm4(proxy.getClass(), method);

                assert paramNames.length == args.length;

                serviceUrlBuilder.append("http://").append(serviceNamespace);
                serviceUrlBuilder.append("/").append(servicePath);
                serviceUrlBuilder.append("?");

                for (int i = 0; i < paramNames.length; i++) {
                    serviceUrlBuilder.append(paramNames[i]).append("=").append(args[i]).append("&");
                }

                String serviceUrl = serviceUrlBuilder.toString();
                logger.info("service url = {}", serviceUrl);

                String result = rpcRestClient.getForObject(serviceUrl, String.class);
                return JsonUtil.decode(result, method.getReturnType());
            }
        });

        return (T) enhancer.create();
    }

    /**
     * 获取指定类指定方法的参数名
     *
     * @param clazz  要获取参数名的方法所属的类
     * @param method 要获取参数名的方法
     * @return 按参数顺序排列的参数名列表, 如果没有参数, 则返回空数组
     * @see <a href="http://blog.csdn.net/mhmyqn/article/details/47294485"> java如何获取方法参数名 </a>
     */
    public static String[] getMethodParameterNamesByAsm4(Class<?> clazz, final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes == null || parameterTypes.length == 0) {
            return new String[]{};
        }
        final Type[] types = new Type[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            types[i] = Type.getType(parameterTypes[i]);
        }
        final String[] parameterNames = new String[parameterTypes.length];

        String className = clazz.getName();
        int lastDotIndex = className.lastIndexOf(".");
        className = className.substring(lastDotIndex + 1) + ".class";
        InputStream is = clazz.getResourceAsStream(className);
        // 使用 asm4 作字节码分析, 提取方法名
        try {
            ClassReader classReader = new ClassReader(is);
            classReader.accept(new ClassVisitor(Opcodes.ASM4) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    // 只处理指定的方法
                    Type[] argumentTypes = Type.getArgumentTypes(desc);
                    if (!method.getName().equals(name) || !Arrays.equals(argumentTypes, types)) {
                        return null;
                    }
                    return new MethodVisitor(Opcodes.ASM4) {
                        @Override
                        public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                            // 静态方法第一个参数就是方法的参数, 如果是实例方法, 第一个参数是this
                            if (Modifier.isStatic(method.getModifiers())) {
                                parameterNames[index] = name;
                            } else if (index > 0) {
                                parameterNames[index - 1] = name;
                            }
                        }
                    };

                }
            }, 0);
        } catch (IOException e) {
            logger.error("字节码分析出现异常, class = {}, method = {}", clazz.getName(), method.getName());
        }
        return parameterNames;
    }

}
