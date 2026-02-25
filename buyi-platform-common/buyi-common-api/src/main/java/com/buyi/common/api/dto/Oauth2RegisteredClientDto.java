package com.buyi.common.api.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
@Accessors(chain = true)
public class Oauth2RegisteredClientDto implements Serializable{

    private static final long serialVersionUID = 1L;

    private String id;

    private String clientId;


    private LocalDateTime clientIdIssuedAt;


    private String clientSecret;


    private LocalDateTime clientSecretExpiresAt;


    private String clientName;


    private String clientAuthenticationMethods;


    private String authorizationGrantTypes;


    private String redirectUris;


    private String postLogoutRedirectUris;


    private String scopes;


    private String clientSettings;

    private String tokenSettings;
}
