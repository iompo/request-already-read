package org.acme.authentication;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.Dependent;
import javax.servlet.http.Cookie;

@Dependent
public class CookieFactory {

    @ConfigProperty(name = "quarkus.http.auth.form.cookie-name")
    String authCookieName;

    public Cookie createEmptyExpiredAuthCookie() {
        Cookie customCookie = new Cookie(authCookieName, null);
        customCookie.setMaxAge(0);
        customCookie.setHttpOnly(false);

        return customCookie;
    }

}
