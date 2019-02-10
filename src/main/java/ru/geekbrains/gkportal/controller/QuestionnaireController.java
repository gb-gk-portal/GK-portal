package ru.geekbrains.gkportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.geekbrains.gkportal.dto.AnswerResultDTO;
import ru.geekbrains.gkportal.entity.Contact;
import ru.geekbrains.gkportal.entity.questionnaire.Question;
import ru.geekbrains.gkportal.entity.questionnaire.Questionnaire;
import ru.geekbrains.gkportal.security.IsAdmin;
import ru.geekbrains.gkportal.service.*;

import java.util.Comparator;


@Controller
@RequestMapping("questionnaire")
public class QuestionnaireController {

    private QuestionnaireService service;
    private ContactService contactService;
    private AnswerResultService answerResultService;
    private AuthenticateService authenticateService;
    private AccountService accountService;


    @Autowired
    public void setAuthenticateService(AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setService(QuestionnaireService service) {
        this.service = service;
    }

    @Autowired
    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }

    @Autowired
    public void setAnswerResultService(AnswerResultService answerResultService) {
        this.answerResultService = answerResultService;
    }

    @IsAdmin
    @GetMapping("result") //http://localhost/questionnaire/result?questionnaireId=bb2248ae-2d7e-427d-85ef-7b85888f0319
    public String showQuestionnaireResults(@RequestParam String questionnaireId, Model model) {
        Questionnaire questionnaire = service.findByIdAndSortQuestionsAndAnswers(questionnaireId);
        model.addAttribute("questionnaire", questionnaire);
        model.addAttribute("contactList", contactService.findAll());
        return "questionnaire-result";
    }

    @GetMapping
    public String showQuestionnaire(@RequestParam(required = false) String questionnaireId, Model model) {
        if (!authenticateService.isCurrentUserAuthenticated()) return "403";
        if (questionnaireId == null) {
            model.addAttribute("questionnaireList", service.findAll());
            return "questionnaire";
        }

        Questionnaire questionnaire;

        if ((questionnaire = service.findByIdAndSortAnswers(questionnaireId)) == null) {
            model.addAttribute("notFoundNumber", questionnaireId);
            model.addAttribute("questionnaireList", service.findAll());
            return "questionnaire";
        }

        questionnaire.getQuestions().sort(Comparator.comparingInt(Question::getSortNumber));
        AnswerResultDTO form = new AnswerResultDTO(questionnaire.getQuestions(), questionnaireId);
        model.addAttribute("questionnaire", questionnaire);
        model.addAttribute("form", form);
        return "questionnaire";
    }

    @PostMapping
    public String getQuestionnaire(@ModelAttribute("form") AnswerResultDTO form, Model model, RedirectAttributes redirectAttributes) throws Throwable {
        model.addAttribute("completed", "Данные записаны");
        if (authenticateService.isCurrentUserAuthenticated()) {
            Contact contact = accountService.getContactByLogin(authenticateService.getCurrentUser().getUsername());
            answerResultService.saveAnswerResultDTO(form, contact);
            return "redirect:/questionnaire";
        } else return "403";
    }


}

