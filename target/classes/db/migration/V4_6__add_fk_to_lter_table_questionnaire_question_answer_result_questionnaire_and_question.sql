#SET foreign_key_checks = 0;

ALTER TABLE `support_boot_db`.`questionnaire_question_answer_result`
  DROP FOREIGN KEY `fk_answer_result_answer_line`;
ALTER TABLE `support_boot_db`.`questionnaire_question_answer_result`
  CHANGE COLUMN `questionnaire_question_answer_id` `answer_id` CHAR(36) NOT NULL COMMENT 'ответ' ,
  ADD COLUMN `questionnaire_id` CHAR(36) NOT NULL AFTER `contact_id`,
  ADD COLUMN `question_id` CHAR(36) NOT NULL AFTER `questionnaire_id`,
  ADD INDEX `fk_answer_contact_idx` (`contact_id` ASC),
  ADD INDEX `fk_answer_questionnaire_idx` (`questionnaire_id` ASC),
  ADD INDEX `fk_answer_question_idx` (`question_id` ASC);

SET foreign_key_checks = 0;
ALTER TABLE `support_boot_db`.`questionnaire_question_answer_result`
  ADD CONSTRAINT `fk_answer_result_answer_line`
FOREIGN KEY (`answer_id`)
REFERENCES `support_boot_db`.`questionnaire_question_answer` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_answer_contact`
FOREIGN KEY (`contact_id`)
REFERENCES `support_boot_db`.`contact` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_answer_questionnaire`
FOREIGN KEY (`questionnaire_id`)
REFERENCES `support_boot_db`.`questionnaire` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_answer_question`
FOREIGN KEY (`question_id`)
REFERENCES `support_boot_db`.`questionnaire_question` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
SET foreign_key_checks = 1;
