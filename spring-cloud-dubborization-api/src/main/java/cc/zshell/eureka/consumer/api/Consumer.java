package cc.zshell.eureka.consumer.api;

import java.lang.annotation.*;

/**
 * 用于 rpc 风格的 eureka client 服务消费
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Consumer {

    // namespace 不得为空, 没有默认值
    String value();

}
