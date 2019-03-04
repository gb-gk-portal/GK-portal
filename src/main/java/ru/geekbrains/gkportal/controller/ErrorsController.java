package ru.geekbrains.gkportal.controller;

import org.apache.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping
public class ErrorsController implements ErrorController {

    private static final Logger logger = Logger.getLogger(ErrorsController.class);

    @Override
    public String getErrorPath() {
        return "error";
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {

                if (logger.isDebugEnabled()) {
                    logger.debug("404");
                }
                return "errors/404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("500");
                }
                return "errors/500";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("403");
                }
                return "errors/403";
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("404");
        }
        return "errors/404";
    }
}
