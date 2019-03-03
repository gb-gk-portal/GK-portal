package ru.geekbrains.gkportal.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerErrorException;
import ru.geekbrains.gkportal.service.AccountService;
import ru.geekbrains.gkportal.service.RoleService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ru.geekbrains.gkportal.config.TemplateNameConst.returnShablon;

@Controller
@RequestMapping("/test")
public class TestWebController {

    private static final Logger logger = Logger.getLogger(TestWebController.class);

    private AccountService accountService;
    private RoleService roleService;

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("uuidGenerator")
    public String permitAllPage(Model model) {

        List<String> uuidList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            uuidList.add(UUID.randomUUID().toString());
        }

        model.addAttribute("uuids", uuidList);
//        При изменение данных можно использовать для тестирвоания создания ролей и пользователей
//        Role role = new Role();
//        role.setDescription("habitant");
//        role = roleService.save(role);
//
//        Account habitant = new Account();
//        habitant.setLogin("habitant_login");
//        habitant.setActive(true);
//        habitant.setConfirmed(true);
//        habitant.setPasswordHash("$2a$10$NKFyj/dcM.HCT52A2jO/k.Vq52X8F1Yhc2wC2MOaWzkztDcyM6hYu");
//        habitant.setRoles(Collections.singletonList(role));
//        accountService.save(habitant);
//
//        Role roleManager = new Role();
//        roleManager.setDescription("manager");
//        roleService.save(role);

        return returnShablon(model, "test/test_page");
    }

    @GetMapping("manager")
    public String managerPage() {
        return "/test/test_page";
    }

    @GetMapping("serverErr")
    public String ServerErr() {
        throw new ServerErrorException("test Exception", new Throwable());
    }
}
