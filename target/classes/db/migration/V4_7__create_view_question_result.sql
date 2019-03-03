DELIMITER $$



 DROP VIEW IF EXISTS `questionnaire_result_view`$$


CREATE  VIEW `questionnaire_result_view` AS (

SELECT r.questionnaire_id,`r`.`question_id`,`r`.`answer_id`,
MIN(`q`.`name`) AS `question_name`,
  MIN(`a`.`name`) AS `answer_name`,
  COUNT(0)  as `vote_count`,
  SUM(csq.square) as `summ_square`
FROM ((`questionnaire_question` `q`
    LEFT JOIN `questionnaire_question_answer` `a`
      ON ((`q`.`id` = `a`.`questionnaire_question_id`)))
   LEFT JOIN `questionnaire_question_answer_result` `r`
     ON (((`r`.`question_id` = `q`.`id`)
          AND (`r`.`answer_id` = `a`.`id`))))
          LEFT JOIN (
          SELECT contact_id, SUM(square*(percentage_of_owner/100)) AS square FROM ownership o
LEFT JOIN ownership_type ot ON o.`ownership_type_id`=ot.id
WHERE ot.is_use_on_questionnaire=1
GROUP BY contact_id

) csq ON r.`contact_id`=csq.contact_id
GROUP BY r.questionnaire_id,`r`.`question_id`,`r`.`answer_id`


  )$$


DELIMITER ;