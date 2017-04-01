package com.csrf.anti.security.tools;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Read/write the session cookie/ anti csrf cookie
 */
public class CookieUtils {
    /**
     * Search for the given list of cookies for a cookie that match the given name, aka <b>cookieId</b>
     * @return the requested cookie value, or null if none found.
     */
    public static String getCookieValue(String cookieId, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(cookieId)) {
                    return c.getValue();
                }
            }
        }
        return null;
    }
}
