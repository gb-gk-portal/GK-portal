ALTER TABLE `support_boot_db`.`questionnaire_contact_confirm`
ADD COLUMN `questionnaire_confirmed_type_id` CHAR(36) DEFAULT '1b4976f3-8a81-45d0-b3b1-9f31dfafaad5';

ALTER TABLE `support_boot_db`.`questionnaire_contact_confirm`
ADD CONSTRAINT `fk_questionnaire_contact_confirm_confirmed_type`
  FOREIGN KEY (`questionnaire_confirmed_type_id`)
  REFERENCES `support_boot_db`.`questionnaire_confirmed_type` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
