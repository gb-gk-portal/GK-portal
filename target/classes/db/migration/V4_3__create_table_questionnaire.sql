/*
SQLyog Ultimate v12.5.1 (64 bit)
MySQL - 5.7.24-log : Database - support_boot_db
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `questionnaire` */

DROP TABLE IF EXISTS `questionnaire`;

CREATE TABLE `questionnaire` (
  `id`              char(36)      NOT NULL,
  `name`            varchar(255)  NOT NULL COMMENT 'Название опроса',
  `start_date`      date          NOT NULL,
  `end_date`        date          NOT NULL,
  `description`     varchar(4000) NOT NULL COMMENT 'Описание для вывода на страницу опроса',
  `open`            bit(1)        NOT NULL
  COMMENT 'Не требует зарегистрированного пользователя',
  `active`          bit(1)        NOT NULL
  COMMENT 'Ещё активно или уже нет',
  `in_build_num`    bit(1)        NOT NULL
  COMMENT 'В строительных номерах?',
  `use_real_estate` bit(1)        NOT NULL
  COMMENT 'С учётом недвижимости?',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `questionnaire_contact_confirm` */

DROP TABLE IF EXISTS `questionnaire_contact_confirm`;

CREATE TABLE `questionnaire_contact_confirm` (
  `id`               char(36) NOT NULL,
  `questionnaire_id` char(36) NOT NULL COMMENT 'опрос',
  `contact_id`       char(36) NOT NULL COMMENT 'контакт',
  `confirm_code`     char(36) NOT NULL COMMENT 'код подтверждения',
  `confirmed`        bit(1)   NOT NULL
  COMMENT 'подтвержден?',
  PRIMARY KEY (`id`),
  KEY `fk_questionnaire_contact_confirm_questionnaire1_idx` (`questionnaire_id`),
  KEY `fk_questionnaire_contact_confirm_contact1_idx` (`contact_id`),
  CONSTRAINT `fk_questionnaire_contact_confirm_contact1` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`id`),
  CONSTRAINT `fk_questionnaire_contact_confirm_questionnaire1` FOREIGN KEY (`questionnaire_id`) REFERENCES `questionnaire` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `questionnaire_question` */

DROP TABLE IF EXISTS `questionnaire_question`;

CREATE TABLE `questionnaire_question` (
  `id`               char(36)   NOT NULL,
  `questionnaire_id` char(36)   NOT NULL COMMENT 'id опроса',
  `sort_number`      int(11)    NOT NULL COMMENT 'сортировка',
  `name`             text       NOT NULL COMMENT 'вопрос',
  `required`         bit(1)     NOT NULL
  COMMENT 'обязательный ответ',
  `single`           bit(1)     NOT NULL
  COMMENT 'одиночный выбор',
  `external_number`  varchar(5) NOT NULL COMMENT 'внешний номер вопроса',
  PRIMARY KEY (`id`),
  KEY `FK_form_id_idx` (`questionnaire_id`),
  CONSTRAINT `fk_question_form` FOREIGN KEY (`questionnaire_id`) REFERENCES `questionnaire` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `questionnaire_question_answer` */

DROP TABLE IF EXISTS `questionnaire_question_answer`;

CREATE TABLE `questionnaire_question_answer` (
  `id` char(36) NOT NULL,
  `name` varchar(255) NOT NULL COMMENT 'ответ',
  `questionnaire_question_id` char(36) NOT NULL COMMENT 'id вопроса',
  `sort_number` int(11) NOT NULL COMMENT 'сортировка',
  PRIMARY KEY (`id`),
  KEY `question_id_question_idx` (`questionnaire_question_id`),
  CONSTRAINT `fk_question_answer` FOREIGN KEY (`questionnaire_question_id`) REFERENCES `questionnaire_question` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `questionnaire_question_answer_result` */

DROP TABLE IF EXISTS `questionnaire_question_answer_result`;

CREATE TABLE `questionnaire_question_answer_result` (
  `id` char(36) NOT NULL,
  #   `questionnaire_id` char(36) NOT NULL COMMENT 'опрос',
  #   `questionnaire_question_id` char(36) NOT NULL COMMENT 'вопрос',
  `questionnaire_question_answer_id` char(36) NOT NULL COMMENT 'ответ',
     `contact_id` char(36) NOT NULL COMMENT 'контакт',

  PRIMARY KEY (`id`),
  #   KEY `fk_answer_line_answer_result_idx` (`questionnaire_id`),
  #   KEY `fk_questionnaire_question_asnwer_result_osntact1_idx` (`contact_id`),
  CONSTRAINT `fk_answer_result_answer_line` FOREIGN KEY (`questionnaire_question_answer_id`) REFERENCES `questionnaire_question_answer` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE #,
  #   CONSTRAINT `fk_questionnaire_question_answer_results_contact1` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `real_estate` */

DROP TABLE IF EXISTS `real_estate`;

CREATE TABLE `real_estate` (
  `id`                  char(36)      NOT NULL,
  `flats`               bit(1)        NOT NULL
  COMMENT 'это квартира?',
  `commercial`          bit(1)        NOT NULL
  COMMENT 'это нежилое помещение?',
  `parking`             bit(1)        NOT NULL
  COMMENT 'это машиноместо?',
  `build_num`           bit(1)        NOT NULL
  COMMENT 'номер строительный?',
  `house_num`           int(11)                DEFAULT NULL
  COMMENT 'номер дома почтовый',
  `house_build_num`     int(11) DEFAULT NULL COMMENT 'номер дома строительный',
  `number`              varchar(20) DEFAULT NULL COMMENT 'номер объекта',
  `build_number`        varchar(20) DEFAULT NULL COMMENT 'строительный номер объекта',
  `square`              decimal(10,2) NOT NULL COMMENT 'площадь',
  `percentage_of_owner` int(11)       NOT NULL DEFAULT '100' COMMENT '% собственности',
  `contact_id`          char(36)      NOT NULL COMMENT 'собствениик',
  PRIMARY KEY (`id`),
  KEY `fk_real_estate_contact1_idx` (`contact_id`),
  CONSTRAINT `fk_real_estate_contact1` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
