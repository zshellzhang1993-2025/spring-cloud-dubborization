package cc.zshell.eureka.spring;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;

/**
 * 扫描所有的 beans, 动态代理所有的 {@link Reference}
 */
public class ReferenceAnnotationProcessor implements BeanPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ReferenceAnnotationProcessor.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        parseFields(bean, bean.getClass().getDeclaredFields());
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    private void parseFields(final Object bean, final Field[] fields) {
        for (final Field field : fields) {
            Reference annotation = AnnotationUtils.getAnnotation(field, Reference.class);
            if (annotation == null) continue;

            String serviceNamespace = annotation.value();

            if (Strings.isNullOrEmpty(serviceNamespace)) {
                throw new RuntimeException("服务namespace必须指定, 否则无法发现服务暴露地址");
            }

            Object proxyConsumer = HttpProxyGenerator.generate(field.getType(), serviceNamespace);
            setField(field, bean, proxyConsumer);
        }

    }

    private void setField(Field field, Object bean, Object param) {
        try {
            field.setAccessible(true);
            field.set(bean, param);
        } catch (IllegalAccessException e) {
            logger.error("设置配置属性失败", e);
        }
    }

}
