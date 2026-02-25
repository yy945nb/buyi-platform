package com.buyi.common.oauth.authorization;

import com.buyi.common.oauth.util.AccessTokenUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


public class WebFluxAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Value("${authorization.accesstoken.rsa.key.public:MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDI90D8sRwU5rwagj9fgNQAFH/Ws03jR+qYUUtA3iI05IaqDLrVDdvMQU446/6c+nyJBtdO3P95+dLg7UVQn1bQSj1wLWa5nuJvTh5paBe1XWZj/HmISTpq+OhyGKmX5xNRU96fDld03JyrgEbmHb9T8jks7g5FhKmZmLJBeRTpoQIDAQAB}")
    private String publicKey;

    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        ServerWebExchange exchange = authorizationContext.getExchange();

        String authorizationToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION); // 从Header里取出token的值
        if (!StringUtils.hasText(authorizationToken)) {
            logger.warn("当前请求头Authorization中的值不存在");
            return Mono.just(new AuthorizationDecision(true));
        }
        if (!authorizationToken.startsWith("Basic ")) {
            boolean verifyResult = AccessTokenUtils.verifyAccessToken(authorizationToken, publicKey);
            if (!verifyResult) {
                return Mono.just(new AuthorizationDecision(false));
            }
        }

        return Mono.just(new AuthorizationDecision(true));
    }
}
