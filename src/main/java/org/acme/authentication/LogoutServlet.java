package org.acme.authentication;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/logoutApp")
public class LogoutServlet extends HttpServlet {

    private static final String LOGIN_PAGE = "/login.html";

    @Inject
    CookieFactory cookieFactory;

    private final FailedServletOperation failedServletOperation;

    public LogoutServlet() {
        this.failedServletOperation = new FailedServletOperation();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getSession().invalidate();
            request.logout();

            Cookie emptyExpiredCookie = cookieFactory.createEmptyExpiredAuthCookie();
            response.addCookie(emptyExpiredCookie);

            response.sendRedirect(request.getContextPath() + LOGIN_PAGE);
        } catch (IOException | ServletException e) {
            failedServletOperation.onError(e, response);
        }
    }

}

