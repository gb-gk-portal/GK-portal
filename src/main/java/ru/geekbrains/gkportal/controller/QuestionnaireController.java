package ru.geekbrains.gkportal.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.gkportal.dto.AnswerResultDTO;
import ru.geekbrains.gkportal.dto.QuestionResultFromView;
import ru.geekbrains.gkportal.entity.Contact;
import ru.geekbrains.gkportal.entity.questionnaire.Question;
import ru.geekbrains.gkportal.entity.questionnaire.Questionnaire;
import ru.geekbrains.gkportal.security.IsAdmin;
import ru.geekbrains.gkportal.security.IsAuthenticated;
import ru.geekbrains.gkportal.service.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static ru.geekbrains.gkportal.config.TemplateNameConst.*;


@Controller
@RequestMapping("questionnaire")
public class QuestionnaireController {

    private static final Logger logger = Logger.getLogger(QuestionnaireController.class);

    private QuestionnaireService questionnaireService;
    private ContactService contactService;
    private AnswerResultService answerResultService;
    private AccountService accountService;
    private AuthenticateService authenticateService;


    @Autowired
    public void setAuthenticateService(AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setQuestionnaireService(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
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
    @GetMapping("result")
    public String showQuestionnaireResults(@RequestParam String questionnaireId, Model model) {
        Questionnaire questionnaire = questionnaireService.findByIdAndSortQuestionsAndAnswers(questionnaireId);

        List<Contact> contactList;
        if (questionnaire.isUseRealEstate()) {
            contactList = contactService.findAllByConfirmQuestionnaireId(questionnaireId);
        } else {
            contactList = questionnaireService.findAllContactByQuestionnaireID(questionnaireId);
        }


        model.addAttribute("questionnaire", questionnaire);
        model.addAttribute("contactList", contactList);
        if (questionnaire.isUseRealEstate())
            model.addAttribute("confirmedCount", contactService.countQuestionnaireContactConfirm(contactList));
        return returnShablon(model, QUESTIONNAIRE_RESULT_FORM);
    }

    @IsAdmin
    @GetMapping("questionnaire-result-datatable")
    public String showQuestionnaireResultsDataTable(@RequestParam String questionnaireId, Model model) {
        long t = System.currentTimeMillis();

        List<Contact> contactList = contactService.findAllByConfirmQuestionnaireId(questionnaireId);

        model.addAttribute("questionnaireName", questionnaireService.findQuestionnaireNameById(questionnaireId));
        model.addAttribute("contactList", contactList);        // TODO: 20.02.19 облегчить запросы , вероятно сделать нативными
        model.addAttribute("confirmedCount", contactService.countQuestionnaireContactConfirm(contactList));

        logger.info("Время обработки showQuestionnaireResultsDataTable " + (System.currentTimeMillis() - t));

        return returnShablon(model, QUESTIONNAIRE_RESULT_FIND_FORM);
    }

    @GetMapping("pie")
    public String showQuestionnairePieResults(@RequestParam String questionnaireId, Model model) {
        List<QuestionResultFromView> qr = questionnaireService.getQuestionaryResultsForPieDiograms(questionnaireId);
        model.addAttribute("results", qr);
        return returnShablon(model, QUESTIONNAIRE_PIE);
    }

    @IsAuthenticated
    @GetMapping
    public String showQuestionnaire(@RequestParam(required = false) String questionnaireId, Model model) {
//        if (!authenticateService.isCurrentUserAuthenticated()) return "403";
        if (questionnaireId == null) {
            model.addAttribute("questionnaireList", questionnaireService.findAll());
            return returnShablon(model, QUESTIONNAIRE_LIST_FORM);
        }

        Questionnaire questionnaire;

        if ((questionnaire = questionnaireService.findByIdAndSortAnswers(questionnaireId)) == null) {
            model.addAttribute("notFoundNumber", questionnaireId);
            model.addAttribute("questionnaireList", questionnaireService.findAll());
            return returnShablon(model, QUESTIONNAIRE_LIST_FORM);
        }

        questionnaire.getQuestions().sort(Comparator.comparingInt(Question::getSortNumber));
        AnswerResultDTO form = new AnswerResultDTO(questionnaire.getQuestions(), questionnaireId);
        model.addAttribute("questionnaire", questionnaire);
        model.addAttribute("form", form);
        return returnShablon(model, QUESTIONNAIRE_LIST_FORM);
    }

    @IsAuthenticated
    @PostMapping
    public String getQuestionnaire(@ModelAttribute("form") AnswerResultDTO form, Model model) throws Throwable {
        model.addAttribute("completed", "Данные записаны");

        if (authenticateService.isCurrentUserAuthenticated()) {
            Contact contact = accountService.getContactByLogin(authenticateService.getCurrentUser().getUsername());
            answerResultService.saveAnswerResultDTO(form, contact);
            return "redirect:/questionnaire";
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("403");
            }
            return "403";
        }
    }


    //    @IsAdmin
    @GetMapping("add")
    public String addQuestionnaire(Model model) { //todo добавить редактирование @RequestParam (required = false) String questionnaireId,
        model.addAttribute("questionnaire", questionnaireService.createEmptyQuestion(1, 2));
        return returnShablon(model, QUESTIONNAIRE_ADD_FORM);
    }

    @PostMapping("saveQuestionnaire")
    public String saveQuestionnaire(@ModelAttribute("questionnaire") Questionnaire questionnaire, Model model) {

        if (questionnaire.getFrom().isBefore(LocalDateTime.now()) && questionnaire.getTo().isAfter(LocalDateTime.now())) {
            questionnaire.setActive(true);
        }

        int[] idxQuestion = {1};
        questionnaire.getQuestions().forEach(question -> {
            question.setSingle(true);
            question.setQuestionnaire(questionnaire);
            question.setExternalNumber(idxQuestion[0]);
            idxQuestion[0]++;

            int[] idxAnswer = {1};
            question.getAnswers().forEach(answer -> {
                answer.setQuestion(question);
                answer.setSortNumber(idxAnswer[0]);
                idxAnswer[0]++;
            });
        });

        questionnaireService.checkedAndSave(questionnaire);


        return "redirect:/questionnaire?questionnaireId="+questionnaire.getUuid();
    }

}

