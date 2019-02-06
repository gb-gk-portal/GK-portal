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
/*Table structure for table `account` */

DROP TABLE IF EXISTS `account`;

CREATE TABLE `account` (
  `id` char(36) NOT NULL,
  `login` varchar(45) NOT NULL COMMENT 'email-login',
  `confirmed` bit(1) NOT NULL COMMENT 'подтверждён почтой',
  `active` bit(1) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `contact_id` char(36),
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_UNIQUE` (`login`),
  KEY `fk_account_users1_idx` (`contact_id`),
  CONSTRAINT `fk_account_users1` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `account_role` */

DROP TABLE IF EXISTS `account_role`;

CREATE TABLE `account_role` (
  `account_id` char(36) NOT NULL,
  `role_id` char(36) NOT NULL,
  PRIMARY KEY (`account_id`,`role_id`),
  KEY `fk_account-roles_account1_idx` (`account_id`),
  KEY `fk_account-roles_roles1_idx` (`role_id`),
  CONSTRAINT `fk_account-roles_account1` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
  CONSTRAINT `fk_account-roles_roles1` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `build_porch_config` */

DROP TABLE IF EXISTS `build_porch_config`;

CREATE TABLE `build_porch_config` (
  `housing` int(11) NOT NULL COMMENT 'номер дома',
  `porch` int(11) NOT NULL COMMENT 'номер подъезда',
  `floors_count` int(11) NOT NULL COMMENT 'кол-во этажей',
  `flat_quantity_floor` int(11) NOT NULL COMMENT 'кол-во квартир на этаже',
  `flat_from_floor` int(11) NOT NULL COMMENT 'с какого этажа начинаются квартиры',
  `flat_quantity_start_floor` int(11) NOT NULL COMMENT 'кол-во квартир на стартовом этаже',
  `record_built` bit(1) NOT NULL DEFAULT b'0' COMMENT 'записи квартир созданы, фильтр для отбора рабочих записей',
  `porch_num_from_right` bit(1) NOT NULL DEFAULT b'0' COMMENT 'нумерация квартир справа, а не слева внутри подъезда как везде',
  `housing_num_from_right` bit(1) NOT NULL DEFAULT b'0' COMMENT 'строительная нумерация справа по этажно через весь дом, а не слева как в остальных',
  `ident_flat_count` int(11) NOT NULL DEFAULT '0' COMMENT 'квартир с известными собственниками',
  `build_housing` int(11) NOT NULL COMMENT 'строительный номер дома',
  `build_porch` int(11) NOT NULL COMMENT 'строительный номер подъезда, про запас',
  `all_flat_count` int(11) GENERATED ALWAYS AS ((((`floors_count` - `flat_from_floor`) * `flat_quantity_floor`) + `flat_quantity_start_floor`)) VIRTUAL COMMENT 'всего квартир в подъезде - вычисляемое поле',
  PRIMARY KEY (`housing`,`porch`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `communication` */

DROP TABLE IF EXISTS `communication`;

CREATE TABLE `communication` (
  `id` char(36) NOT NULL,
  `communication_type_id` char(36) NOT NULL COMMENT 'вид связи',
  `contact_id` char(36) NOT NULL COMMENT 'чей',
  `identify` varchar(255) NOT NULL COMMENT 'логин связи',
  `description` varchar(255) NOT NULL COMMENT 'описание - типа домашняя почта',
  `confirmed` bit(1) NOT NULL,
  `confirm_code_date` date NOT NULL,
  `confirm_code` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_communication_communication_type1_idx` (`communication_type_id`),
  KEY `fk_communication_users1_idx` (`contact_id`),
  CONSTRAINT `fk_communication_communication_type1` FOREIGN KEY (`communication_type_id`) REFERENCES `communication_type` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
  CONSTRAINT `fk_communication_users1` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `communication_type` */

DROP TABLE IF EXISTS `communication_type`;

CREATE TABLE `communication_type` (
  `id` char(36) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `contact` */

DROP TABLE IF EXISTS `contact`;

CREATE TABLE `contact` (
  `id` char(36) NOT NULL,
  `contact_type_id` char(36) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `middle_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_contact_type_id1_idx` (`contact_type_id`),
  CONSTRAINT `fk_contact_type_id1` FOREIGN KEY (`contact_type_id`) REFERENCES `contact_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `contact_flat` */

DROP TABLE IF EXISTS `contact_flat`;

CREATE TABLE `contact_flat` (
  `contact_id` char(36) NOT NULL,
  `flat_id` char(36) NOT NULL,
  PRIMARY KEY (`contact_id`,`flat_id`),
  KEY `fk_users_has_flats_flats1_idx` (`flat_id`),
  CONSTRAINT `fk_users_has_flats_flats1` FOREIGN KEY (`flat_id`) REFERENCES `flat` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_has_flats_users1` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `contact_type` */

DROP TABLE IF EXISTS `contact_type`;

CREATE TABLE `contact_type` (
  `id` char(36) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `flat` */

DROP TABLE IF EXISTS `flat`;

CREATE TABLE `flat` (
  `id` char(36) NOT NULL COMMENT 'id',
  `square` float DEFAULT NULL COMMENT 'кв.м. без учёта лоджии/балкона',
  `rooms` int(11) DEFAULT NULL COMMENT 'кол-во комнат',
  `owners_count` int(11) DEFAULT NULL COMMENT 'кол-во собственников',
  `house` int(11) NOT NULL COMMENT 'дом',
  `porch` int(11) NOT NULL COMMENT 'подъезд',
  `floor` int(11) NOT NULL COMMENT 'этаж',
  `flat_num` int(11) NOT NULL COMMENT 'квартира',
  `riser` int(11) NOT NULL DEFAULT '0' COMMENT 'номер стояка',
  `house_build` int(11) NOT NULL COMMENT 'строительный номер дома',
  `porch_build` int(11) NOT NULL COMMENT 'строительный номер подъезда',
  `flat_num_build` int(11) NOT NULL COMMENT 'строительный номер',
  PRIMARY KEY (`id`),
  UNIQUE KEY `num` (`house`,`porch`,`flat_num`),
  KEY `num_build` (`house_build`,`porch_build`,`flat_num_build`)
) ENGINE=InnoDB AUTO_INCREMENT=1698 DEFAULT CHARSET=utf8;

/*Table structure for table `role` */

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` char(36) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;