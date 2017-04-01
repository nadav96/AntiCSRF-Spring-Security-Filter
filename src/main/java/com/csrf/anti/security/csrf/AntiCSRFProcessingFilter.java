package com.csrf.anti.security.csrf;

import com.csrf.anti.security.tools.CookieUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Nadav on 01/04/2017.
 */
public class AntiCSRFProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private String csrfHeaderName = "X-XSRF-TOKEN";
    private String csrfCookieName = "XSRF-TOKEN";

    public AntiCSRFProcessingFilter(RequestMatcher matcher) {
        super(matcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String cookieValue = CookieUtils.getCookieValue(csrfCookieName, request);
        String tokenValue = request.getHeader(this.csrfHeaderName);
        AntiCSRFAuthenticationObject csrfObject = new AntiCSRFAuthenticationObject(cookieValue, tokenValue);
        return this.getAuthenticationManager().authenticate(csrfObject);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        response.addCookie(generateAntiCSRFCookie());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.addCookie(generateAntiCSRFCookie());
        super.unsuccessfulAuthentication(request, response, failed);
    }

    private Cookie generateAntiCSRFCookie() {
        Cookie c = new Cookie(csrfCookieName, UUID.randomUUID().toString());
        c.setPath("/");
        return c;
    }
}
