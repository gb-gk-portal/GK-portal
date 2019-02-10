package ru.geekbrains.gkportal.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geekbrains.gkportal.dto.SystemAccountToOwnerShip;
import ru.geekbrains.gkportal.entity.Contact;
import ru.geekbrains.gkportal.entity.questionnaire.Answer;
import ru.geekbrains.gkportal.entity.questionnaire.Question;
import ru.geekbrains.gkportal.entity.questionnaire.Questionnaire;
import ru.geekbrains.gkportal.entity.questionnaire.QuestionnaireContactConfirm;
import ru.geekbrains.gkportal.repository.QuestionnaireContactConfirmRepository;
import ru.geekbrains.gkportal.repository.QuestionnaireRepository;

import java.util.Comparator;
import java.util.List;

@Service
public class QuestionnaireService {

    private QuestionnaireRepository questionnaireRepository;
    private QuestionnaireContactConfirmRepository questionnaireContactConfirmRepository;
    private MailService mailService;

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Autowired
    public void setQuestionnaireContactConfirmRepository(QuestionnaireContactConfirmRepository questionnaireContactConfirmRepository) {
        this.questionnaireContactConfirmRepository = questionnaireContactConfirmRepository;
    }

    @Autowired
    public void setQuestionnaireRepository(QuestionnaireRepository questionnaireRepository) {
        this.questionnaireRepository = questionnaireRepository;
    }

    public Questionnaire findByIdAndSortAnswers(String id) {
        Questionnaire questionnaire = findById(id);

        if (questionnaire != null) {
            questionnaire.getQuestions().forEach(questionnaireQuestion ->
                    questionnaireQuestion.getAnswers()
                            .sort(Comparator.comparingInt(Answer::getSortNumber)));
        }
        return questionnaire;
    }

    public Questionnaire findByIdAndSortQuestionsAndAnswers(String id) {
        Questionnaire questionnaire = findByIdAndSortAnswers(id);
        questionnaire.getQuestions().sort(Comparator.comparingInt(Question::getSortNumber));
        return questionnaire;
    }

    public Questionnaire findById(String id) {
        return questionnaireRepository.findById(id).orElse(null);
    }

    public Questionnaire save(Questionnaire questionnaire) {
        return questionnaireRepository.save(questionnaire);
    }

    public boolean isQuestionnaireContactExist(Questionnaire questionnaire, Contact contact) {
        return (questionnaireContactConfirmRepository.getByQuestionnaireAndContact(questionnaire, contact) != null);
    }


    public List<Questionnaire> findAll() {
        return questionnaireRepository.findAll();
    }

    public void sendMail(SystemAccountToOwnerShip systemAccount, Contact contact) {


    }

    public QuestionnaireContactConfirm getQuestionnaireContactConfirm(String questionnaireId, Contact contact) {
        return questionnaireContactConfirmRepository.getByQuestionnaireAndContact(findById(questionnaireId), contact);
    }

    public boolean confirmQuetionnaire(Contact contact, String code) {
        QuestionnaireContactConfirm confirm = questionnaireContactConfirmRepository.getByContactAndConfirmCode(contact, code);
        if (confirm != null) {
            confirm.setConfirmed(true);
            questionnaireContactConfirmRepository.save(confirm);
            return true;

        }

        return false;
    }
}
