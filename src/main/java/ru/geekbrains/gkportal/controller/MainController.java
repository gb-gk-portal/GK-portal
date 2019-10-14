package ru.geekbrains.gkportal.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static ru.geekbrains.gkportal.config.TemplateNameConst.OSS4_FORM;
import static ru.geekbrains.gkportal.config.TemplateNameConst.returnShablon;

@Controller
public class MainController {

    private static final Logger logger = Logger.getLogger(MainController.class);

    @GetMapping("/")
    public String main(Model model) {
        return returnShablon(model, OSS4_FORM);
    }
}
