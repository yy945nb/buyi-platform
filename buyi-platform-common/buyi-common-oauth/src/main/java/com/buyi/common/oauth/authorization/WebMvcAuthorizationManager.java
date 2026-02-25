package com.buyi.common.oauth.authorization;

import com.buyi.common.oauth.util.AccessTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.util.StringUtils;

import java.util.function.Supplier;

/**
 * webMvc 授权拦截器
 */
public class WebMvcAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Value("${authorization.accesstoken.rsa.key.public:MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDI90D8sRwU5rwagj9fgNQAFH/Ws03jR+qYUUtA3iI05IaqDLrVDdvMQU446/6c+nyJBtdO3P95+dLg7UVQn1bQSj1wLWa5nuJvTh5paBe1XWZj/HmISTpq+OhyGKmX5xNRU96fDld03JyrgEbmHb9T8jks7g5FhKmZmLJBeRTpoQIDAQAB}")
    private String publicKey;
    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        HttpServletRequest request = object.getRequest();
        String authorizationToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(authorizationToken)) {
            return new AuthorizationDecision(false);
        }

        if (!authorizationToken.startsWith("Basic ")) {
            boolean verifyResult = AccessTokenUtils.verifyAccessToken(authorizationToken, publicKey);
            if (!verifyResult) {
                return new AuthorizationDecision(false);
            }
        }
        return new AuthorizationDecision(true);
    }
}
