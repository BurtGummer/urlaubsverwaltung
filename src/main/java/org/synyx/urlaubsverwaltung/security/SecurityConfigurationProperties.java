package org.synyx.urlaubsverwaltung.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("uv.security")
public class SecurityConfigurationProperties {

    public enum AuthenticationProvider {

        OIDC,
        LDAP,
        ACTIVEDIRECTORY,
        DEFAULT
    }

    private AuthenticationProvider auth = AuthenticationProvider.DEFAULT;

    public AuthenticationProvider getAuth() {
        return auth;
    }

    public void setAuth(AuthenticationProvider auth) {
        this.auth = auth;
    }
}

