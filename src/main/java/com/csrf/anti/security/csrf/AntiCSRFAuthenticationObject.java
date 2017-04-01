package com.csrf.anti.security.csrf;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * Created by Nadav on 01/04/2017.
 */
public class AntiCSRFAuthenticationObject extends AbstractAuthenticationToken {
    private String csrfCookieValue;
    private String csrfTokenValue;

    public AntiCSRFAuthenticationObject(String csrfCookieValue, String csrfTokenValue) {
        super(null);
        this.csrfCookieValue = csrfCookieValue;
        this.csrfTokenValue = csrfTokenValue;
        setAuthenticated(false);
    }

    public AntiCSRFAuthenticationObject() {
        super(null);
        setAuthenticated(true);
    }

    @Override
    public String getCredentials() {
        return this.csrfTokenValue;
    }

    @Override
    public String getPrincipal() {
        return this.csrfCookieValue;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.csrfCookieValue = null;
        this.csrfTokenValue = null;
    }
}
