package cc.zshell.eureka.consumer.api;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

class ConsumerNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("config", new Parser());
        registerBeanDefinitionParser("annotation-driven", new Parser());
    }

    static class Parser extends AbstractSingleBeanDefinitionParser {

        @Override
        protected Class<?> getBeanClass(Element element) {
            return ConsumerAnnotationProcessor.class;
        }

        @Override
        protected boolean shouldGenerateId() {
            return true;
        }

        @Override
        protected void doParse(Element element, BeanDefinitionBuilder builder) {
            if (element.getLocalName().equals("annotation-driven")) {
                builder.addPropertyValue("trimValue", Boolean.valueOf(element.getAttribute("trim-value")));
                return;
            }

            String location = element.getAttribute("files");
            if (StringUtils.hasLength(location)) {
                String[] ids = StringUtils.commaDelimitedListToStringArray(location);
                builder.addConstructorArgValue(ids);
            }

            String order = element.getAttribute("order");
            if (StringUtils.hasLength(order)) {
                builder.addPropertyValue("order", Integer.valueOf(order));
            }

            builder.addPropertyValue("timeout", Integer.valueOf(element.getAttribute("timeout")));
            builder.addPropertyValue("ignoreResourceNotFound",
                    Boolean.valueOf(element.getAttribute("ignore-resource-not-found")));

            builder.addPropertyValue("ignoreUnresolvablePlaceholders",
                    Boolean.valueOf(element.getAttribute("ignore-unresolvable")));
            builder.addPropertyValue("trimValue", Boolean.valueOf(element.getAttribute("trim-value")));
        }
    }

}

