package ru.geekbrains.gkportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.geekbrains.gkportal.dto.interfaces.AnswerResultDTO1;
import ru.geekbrains.gkportal.entity.questionnaire.AnswerResult;

import java.util.List;

@Repository
public interface AnswerResultRepository 
        extends JpaRepository<AnswerResult, String> {

    List<AnswerResult> findAllByContact_Uuid(String contactUuid);

    List<AnswerResultDTO1> findAllByQuestionnaire_UuidOrderByQuestion_SortNumber(String questionnaireUuid);


    @Query(value = "SELECT  distinct c.id from questionnaire_question_answer_result a " +
            "left join contact c on a.contact_id = c.id" +
            "" +
            " where a.questionnaire_id = :_questionnaire", nativeQuery = true)
    List<String> findAllDistinctContactByQuestionnaire_Uuid(@Param("_questionnaire") String questionnaireUUID);




}
