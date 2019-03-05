package ru.geekbrains.gkportal.service;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geekbrains.gkportal.dto.AnswerResultDTO;
import ru.geekbrains.gkportal.dto.QuestionnaireContactResult;
import ru.geekbrains.gkportal.dto.interfaces.AnswerResultDTO1;
import ru.geekbrains.gkportal.entity.AbstractEntity;
import ru.geekbrains.gkportal.entity.Contact;
import ru.geekbrains.gkportal.entity.questionnaire.*;
import ru.geekbrains.gkportal.repository.AnswerResultRepository;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AnswerResultService {

    private static final Logger logger = Logger.getLogger(AnswerResultService.class);

    private AnswerResultRepository answerResultRepository;
    private QuestionnaireService questionnaireService;
    private ContactService contactService;

    @Autowired
    public void setAnswerResultRepository(AnswerResultRepository answerResultRepository) {
        this.answerResultRepository = answerResultRepository;
    }

    @Autowired
    public void setQuestionnaireService(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    @Autowired
    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }

    public void saveAll(List<AnswerResult> answerResults) {
        answerResultRepository.saveAll(answerResults);
    }

    //public void saveAll(AnswerResultDTO answerResultDTO) throws Throwable {
    //    saveAnswerResultDTO(answerResultDTO);
    // }

    public List<AnswerResultDTO1> findAllByQuestionnaireUuid(String questionnaireUuid) {
        return answerResultRepository.findAllByQuestionnaire_UuidOrderByQuestion_SortNumber(questionnaireUuid);
    }

    public void saveAnswerResultDTO(AnswerResultDTO answerResultDTO, Contact contact) {
        Questionnaire questionnaire = questionnaireService.findByIdAndSortAnswers(answerResultDTO.getQuestionnaireId());

        Collection<Question> questions = questionnaire.getQuestions();
        QuestionnaireContactConfirm questionnaireContactConfirm;
        // пытаемся убрать баг про несколько подтверждений на один контакт и голосование
        if (contact.getQuestionnaireContactConfirm() == null) {
            questionnaireContactConfirm = QuestionnaireContactConfirm.builder()
                    .questionnaire(questionnaire)
                    .confirmCode(UUID.randomUUID().toString())
                    .contact(contact)
                    .questionnaireConfirmedType(questionnaireService.findQuestionnaireConfirmedTypeByName("не искали"))
                    .build();
        } else {
            questionnaireContactConfirm = contact.getQuestionnaireContactConfirm();
            questionnaireContactConfirm.setConfirmCode(UUID.randomUUID().toString());
        }
        contact.setQuestionnaireContactConfirm(questionnaireContactConfirm);

        Map<String, Answer> answers = new HashMap<>();
        Map<String, Question> questionsMap = new HashMap<>();

        for (Question question : questions) {
            answers.putAll(question.getAnswers().stream().collect(Collectors.toMap(AbstractEntity::getUuid, Function.identity())));
            questionsMap.put(question.getUuid(), question);
        }

        List<AnswerResult> answerResults = new ArrayList<>();

        for (Map.Entry<String, String> entry : answerResultDTO.getQuestionAnswerUuids().entrySet()) {
            Answer answer = answers.get(entry.getValue());
            Question question = questionsMap.get(entry.getKey());
            answerResults.add(new AnswerResult(answer, contact, questionnaire, question));
        }

        saveAll(answerResults);
    }

    public List<QuestionnaireContactResult> getAllByContact(Contact contact) {
        List<QuestionnaireContactResult> questionnaireContactResults = new ArrayList<>();

//        Optional<AnswerResult> answerResultOptional = answerResultRepository.findAllByContact_Uuid(contact.getUuid());
        List<AnswerResult> answerResultList = answerResultRepository.findAllByContact_Uuid(contact.getUuid());

//        if (answerResult.isPresent()){

//            List<AnswerResult> answerResultList = answerResult.map(v-> new ArrayList<>(Arrays.asList(v)))
//                    .orElseGet(() -> new ArrayList<>());

        for (AnswerResult answerResult : answerResultList) {

            QuestionnaireContactResult questionnaireContactResult = QuestionnaireContactResult.builder()
                    .firstNameContact(answerResult.getContact().getFirstName())
                    .middleNameContact(answerResult.getContact().getMiddleName())
                    .lastNameContact(answerResult.getContact().getLastName())
                    .nameQuestionnaire(answerResult.getQuestionnaire().getName())
                    .descriptionQuestionnaire(answerResult.getQuestionnaire().getDescription())
                    .isOpenQuestionnaire(answerResult.getQuestionnaire().isOpen())
                    .startDateQuestionnaire(answerResult.getQuestionnaire().getFrom().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                    .endDateQuestionnaire(answerResult.getQuestionnaire().getTo().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                    .isActiveQuestionnaire(answerResult.getQuestionnaire().isActive())
                    .isInBuildNumQuestionnaire(answerResult.getQuestionnaire().isInBuildNum())
                    .isUseRealEstateQuestionnaire(answerResult.getQuestionnaire().isUseRealEstate())
                    .nameQuestion(answerResult.getQuestion().getName())
                    .externalNumberQuestion(answerResult.getQuestion().getExternalNumber().toString())
                    .sortNumberQuestion(answerResult.getQuestion().getSortNumber())
                    .nameQuestionAnswer(answerResult.getAnswer().getName())
                    .sortNumberQuestionAnswer(answerResult.getAnswer().getSortNumber()).build();

            questionnaireContactResults.add(questionnaireContactResult);
        }
//        }

        return questionnaireContactResults;
    }

    public List<Contact> findAllContactByQuestionnaireID(String questionnaireID) {
        List<String> list = answerResultRepository.findAllDistinctContactByQuestionnaire_Uuid(questionnaireID);
        List<Contact> contactList = new ArrayList<>();
        for (String c : list) {
            contactList.add(contactService.findById(c));
        }
        return contactList;
    }
}
