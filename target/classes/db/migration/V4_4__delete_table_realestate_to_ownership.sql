
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

/*Table structure for table `ownership` */

DROP TABLE IF EXISTS `real_estate`;


/*Table structure for table `ownership_type` */

DROP TABLE IF EXISTS `ownership_type`;

CREATE TABLE `ownership_type` (
  `id` char(36) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` text NOT NULL,
  `is_req_house_num` bit(1) NOT NULL,
  `is_req_percentage_owner` bit(1) NOT NULL,
  `is_req_square` bit(1) NOT NULL,
  `is_use_on_questionnaire` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE `ownership` (
  `id` char(36) NOT NULL,
  `ownership_type_id` char(36) NOT NULL,
  `is_build_num` bit(1) NOT NULL COMMENT 'номер строительный?',
  `house_num` int(11) DEFAULT NULL COMMENT 'номер дома почтовый',
  `house_build_num` int(11) DEFAULT NULL COMMENT 'номер дома строительный',
  `number` varchar(20) DEFAULT NULL COMMENT 'номер объекта',
  `build_number` varchar(20) DEFAULT NULL COMMENT 'строительный номер объекта',
  `square` decimal(10,2) NOT NULL COMMENT 'площадь',
  `percentage_of_owner` int(11) NOT NULL DEFAULT '100' COMMENT '% собственности',
  `contact_id` char(36) NOT NULL COMMENT 'собствениик',
  PRIMARY KEY (`id`),
  KEY `fk_real_estate_contact1_idx` (`contact_id`),
  KEY `fk_ownership_type` (`ownership_type_id`),
  CONSTRAINT `fk_ownership_contact` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`id`),
  CONSTRAINT `fk_ownership_type` FOREIGN KEY (`ownership_type_id`) REFERENCES `ownership_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `ownership` */

/*Data for the table `ownership_type` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
