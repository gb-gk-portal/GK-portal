package ru.geekbrains.gkportal.service;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geekbrains.gkportal.dto.AnswerFromViewDTO;
import ru.geekbrains.gkportal.dto.QuestionResultFromView;
import ru.geekbrains.gkportal.dto.SystemAccountToOwnerShip;
import ru.geekbrains.gkportal.dto.interfaces.QuestionnaireContactConfirmDTO;
import ru.geekbrains.gkportal.dto.interfaces.QuestionnaireDTO;
import ru.geekbrains.gkportal.entity.Contact;
import ru.geekbrains.gkportal.entity.questionnaire.*;
import ru.geekbrains.gkportal.exception.QuestionnaireContactConfirmNotFoundException;
import ru.geekbrains.gkportal.repository.QuestionnaireConfirmedTypeRepository;
import ru.geekbrains.gkportal.repository.QuestionnaireContactConfirmRepository;
import ru.geekbrains.gkportal.repository.QuestionnaireRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

@Service
public class QuestionnaireService {

    private static final Logger logger = Logger.getLogger(QuestionnaireService.class);

    private QuestionnaireRepository questionnaireRepository;
    private QuestionnaireContactConfirmRepository questionnaireContactConfirmRepository;
    private QuestionnaireConfirmedTypeRepository questionnaireConfirmedTypeRepository;
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

    public List<QuestionResultFromView> getQuestionaryResultsForPieDiograms(String questionaryId) {
        List<QuestionnaireRepository.QuestAns> questAnses = questionnaireRepository.getQuestionaryResultByQuestionaryId(questionaryId);
        System.out.println(questAnses.size());
        List<QuestionResultFromView> resultList = new ArrayList<>();
        for (QuestionnaireRepository.QuestAns qa : questAnses
        ) {
            String question = qa.getQuestion_name();
            AnswerFromViewDTO answerDTO = new AnswerFromViewDTO();
            answerDTO.setAnswerName(qa.getAnswer_name());
            answerDTO.setVoteCount(qa.getVote_count());
            boolean isAdded = false;
            for (QuestionResultFromView qr : resultList
            ) {
                if (qr.getQuestionName().equals(question)) {
                    qr.getAnswers().add(answerDTO);
                    isAdded = true;
                    break;
                }
            }
            if (!isAdded) {
                QuestionResultFromView questionResultFromView = new QuestionResultFromView();
                questionResultFromView.setQuestionName(question);
                questionResultFromView.getAnswers().add(answerDTO);
                resultList.add(questionResultFromView);
            }
        }
        System.out.println(resultList.size());
        return resultList;
    }

    @Autowired
    public void setQuestionnaireConfirmedTypeRepository(QuestionnaireConfirmedTypeRepository questionnaireConfirmedTypeRepository) {
        this.questionnaireConfirmedTypeRepository = questionnaireConfirmedTypeRepository;
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

    public String findQuestionnaireNameById(String id) {
        return questionnaireRepository.findNameByUuid(id);

    }

    public Questionnaire findByIdAndSortQuestionsAndAnswers(String id) {
        Questionnaire questionnaire = findByIdAndSortAnswers(id);
        questionnaire.getQuestions().sort(Comparator.comparingInt(Question::getSortNumber));
        return questionnaire;
    }


    public QuestionnaireConfirmedType findQuestionnaireConfirmedTypeByName(String name) {
        return questionnaireConfirmedTypeRepository.findByName(name);
    }

    public QuestionnaireDTO findQuestionnaireDTOById(String uuid) {
        return questionnaireRepository.findByUuid(uuid);
    }

    public Questionnaire findById(String id) {
        return questionnaireRepository.findById(id).orElse(null);
    }

    public Questionnaire save(Questionnaire questionnaire) {
        return questionnaireRepository.save(questionnaire);
    }

    public Questionnaire checkedAndSave(Questionnaire questionnaire) {
        // TODO: 03.03.2019 вынести логику с контроллера

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

    public List<QuestionnaireContactConfirmDTO> findQuestionnaireContactConfirmDTO(String questionnaireId) {
        return questionnaireContactConfirmRepository.findAllByQuestionnaire_UuidOrderByContact(questionnaireId);
    }

    public QuestionnaireContactConfirm changeQuestionnaireConfirmedType(String questionnaireContactConfirmId, String questionnaireConfirmedTypeId) throws Throwable {

        QuestionnaireContactConfirm questionnaireContactConfirm = questionnaireContactConfirmRepository
                .findById(questionnaireContactConfirmId)
                .orElseThrow((Supplier<Throwable>) () -> new QuestionnaireContactConfirmNotFoundException(questionnaireContactConfirmId));

        QuestionnaireConfirmedType questionnaireConfirmedType = questionnaireConfirmedTypeRepository
                .findById(questionnaireConfirmedTypeId)
                .orElseThrow((Supplier<Throwable>) () -> new QuestionnaireContactConfirmNotFoundException(questionnaireContactConfirmId));

        questionnaireContactConfirm.setQuestionnaireConfirmedType(questionnaireConfirmedType);

        return questionnaireContactConfirmRepository.save(questionnaireContactConfirm);


    }

    public boolean confirmQuestionnaire(Contact contact, String code) {
        QuestionnaireContactConfirm confirm = questionnaireContactConfirmRepository.getByContactAndConfirmCode(contact, code);
        if (confirm != null) {
            confirm.setConfirmed(true);
            questionnaireContactConfirmRepository.save(confirm);
            return true;
        }
        return false;
    }


    /**
     * Подгоавливает пустой опрос для вывода на фронт, с предзаполненными данными
     *
     * @param questionCount число вопросов
     * @param answersCount  число ответов ы каждом вопросе
     * @return предзаполненый опрос
     */
    public Questionnaire createEmptyQuestion(int questionCount, int answersCount) {
        Questionnaire questionnaire = Questionnaire.builder()
                .name("Опрос жителей ЖК Город о ...")
                .description("Опрос направлен на ...")
                .questions(new ArrayList<>())
                .from(LocalDateTime.now().withHour(10).withMinute(0))
                .to(LocalDateTime.now().plusMonths(1L).withHour(10).withMinute(0))
                .build();


        for (int i = 0; i < questionCount; i++) {
            Question question = Question.builder()
                    .questionnaire(questionnaire)
                    .answers(new ArrayList<>())
                    .sortNumber(i + 1)
                    .build();
            for (int j = 0; j < answersCount; j++) {
                question.getAnswers().add(new Answer());
            }
            questionnaire.getQuestions().add(question);
        }


        return questionnaire;
    }
}
