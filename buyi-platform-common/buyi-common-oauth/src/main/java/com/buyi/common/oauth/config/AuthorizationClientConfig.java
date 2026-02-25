
package com.buyi.common.oauth.config;

import com.buyi.common.oauth.authorization.WebMvcAuthorizationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;


/**
 * @description: 配置项目的放行策略
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class AuthorizationClientConfig {

    @Bean
    WebMvcAuthorizationManager webMvcAuthorizationManager() {
        return new WebMvcAuthorizationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        //uri放行
        String[] ignoreUrls = new String[]{"/*.html", "/favicon.ico", "/webjars/**", "/*/v3/api-docs**", "/v3/api-docs/**"};
        AntPathRequestMatcher[] array = Arrays.stream(ignoreUrls).map(AntPathRequestMatcher::new).toArray(AntPathRequestMatcher[]::new);
        http.authorizeHttpRequests(authorize ->
                authorize.requestMatchers(array).permitAll()
                        // 鉴权管理器配置
                        .anyRequest().access(webMvcAuthorizationManager()));
        http.cors(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
