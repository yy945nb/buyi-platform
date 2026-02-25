package com.buyi.framework.security.configuration.server.annotation;

import com.buyi.framework.security.configuration.server.AuthorizationServerConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 授权服务注解
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({AuthorizationServerConfiguration.class})
public @interface EnableOAuth2Server {

}
