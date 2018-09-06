package cc.zshell.eureka.provider.api;

import com.google.common.collect.Lists;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * 扫描所有 @Provider 注解的 bean
 */
public class ProviderAnnotationProcessor implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Object> springBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, Object.class);
        for (Object bean : springBeans.values()) {
            Annotation annotation = AnnotationUtils.findAnnotation(bean.getClass(), Provider.class);
            if (annotation == null) {
                continue;
            }

        }
    }


}
