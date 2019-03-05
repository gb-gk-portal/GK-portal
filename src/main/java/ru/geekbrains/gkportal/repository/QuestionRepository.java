package ru.geekbrains.gkportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.geekbrains.gkportal.dto.interfaces.QuestionDTO;
import ru.geekbrains.gkportal.entity.questionnaire.Question;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, String> {

    @Query(
            value = "SELECT q.name FROM support_boot_db.questionnaire_question  q WHERE questionnaire_id = :questionnaireUuid ORDER BY sort_number",
            nativeQuery = true
    )
    List<String> findAllNamesByQuestionnaire_UuidOrderBySortNumber(@Param("questionnaireUuid") String questionnaireUuid);

    List<QuestionDTO> findAllByQuestionnaire_UuidOrderBySortNumber(String questionnaireUuid);
}
