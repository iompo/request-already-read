package org.acme.authentication;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FailedServletOperation {

    private static final Logger LOGGER = Logger.getLogger(FailedServletOperation.class.getName());

    public void onError(Exception e, HttpServletResponse response) {
        LOGGER.log(Level.WARNING, "Servlet execution error", e);
        try {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (IOException ioException) {
            response.reset();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
