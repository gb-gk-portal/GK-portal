package ru.geekbrains.gkportal.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailMessageBuilder {

    private static final String VARIABLE_REGISTRATION_USER = "user";
    private static final String VARIABLE_REGISTRATION_PASSWORD = "password";
    private static final String MAIL_REGISTRATION_PAGE = "registration-mail";

    private TemplateEngine templateEngine;

    @Autowired
    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }


    public String buildRegistrationEmail() {
        Context context = new Context();
        context.setVariable(VARIABLE_REGISTRATION_USER, "Юрий");
        context.setVariable(VARIABLE_REGISTRATION_PASSWORD, "gccasjbc33j4b");
        return templateEngine.process(MAIL_REGISTRATION_PAGE, context);
    }

    public String buildRegistrationEmail(String userName, String url) {
        Context context = new Context();
        context.setVariable(VARIABLE_REGISTRATION_USER, userName);
        context.setVariable(VARIABLE_REGISTRATION_PASSWORD, url);
        return templateEngine.process(MAIL_REGISTRATION_PAGE, context);
    }


// TODO: 22.01.19 раскометировать после появления сущьности User
//    public String buildRegistrationEmail(User user) {
//        Context context = new Context();
//        context.setVariable(VARIABLE_REGISTRATION_USER, user);
//        context.setVariable(VARIABLE_REGISTRATION_PASSWORD, password);
//        return templateEngine.process(MAIL_REGISTRATION_PAGE, context);
//    }
}
