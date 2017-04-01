package com.csrf.anti.security.csrf;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * Created by Nadav on 01/04/2017.
 */
@Component
public class AntiCSRFAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AntiCSRFAuthenticationObject csrfObject = (AntiCSRFAuthenticationObject) authentication;
        if (StringUtils.isBlank(csrfObject.getPrincipal())) {
            throw new BadCredentialsException("No cookie set, seems to be a problem on our side");
        } else if (StringUtils.isBlank(csrfObject.getCredentials())) {
            throw new BadCredentialsException("Possible csrf attack");
        } else if (csrfObject.getPrincipal().equals(csrfObject.getCredentials())) {
            return new AntiCSRFAuthenticationObject();
        }
        else {
            throw new BadCredentialsException("Mismatch between the data, possible brute force?");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (AntiCSRFAuthenticationObject.class.isAssignableFrom(authentication));
    }
}
