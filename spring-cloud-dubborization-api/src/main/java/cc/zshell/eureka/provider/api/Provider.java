package cc.zshell.eureka.provider.api;

import java.lang.annotation.*;

/**
 * 用于 rpc 风格的 eureka client 服务暴露
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Provider {
}
