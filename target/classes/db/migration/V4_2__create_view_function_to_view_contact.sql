DELIMITER $$
DROP FUNCTION IF EXISTS `getContactCommunicationList`$$


CREATE  FUNCTION `getContactCommunicationList`(contactId CHAR(36)) RETURNS text CHARSET utf8
return (SELECT GROUP_CONCAT(CONCAT(com_type.description,':',com.identify)) FROM communication com
JOIN communication_type com_type ON com_type.id=com.communication_type_id
  WHERE com.contact_id=contactId) $$


DROP FUNCTION IF EXISTS `getContactFlatsList` $$

CREATE  FUNCTION `getContactFlatsList`(contactId CHAR(36)) RETURNS text CHARSET utf8
return (SELECT GROUP_CONCAT(CONCAT("дом ",flat.house,"  кв.",flat.flat_num)) FROM contact_flat con_flat
			JOIN flat ON flat.id=con_flat.flat_id
			WHERE con_flat.contact_id=contactId) $$

DROP FUNCTION IF EXISTS `getContactRoleList` $$


CREATE  FUNCTION `getContactRoleList`(contactId CHAR(36)) RETURNS text CHARSET utf8
return (SELECT GROUP_CONCAT(r.description) FROM account_role a_r
JOIN role r ON r.id=a_r.role_id
  WHERE a_r.account_id=contactId) $$



 DROP VIEW IF EXISTS `contact_view`$$

CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `contact_view` AS (select `acc`.`login` AS `login`,`acc`.`confirmed` AS `confirmed`,`acc`.`active` AS `active`,`cont_type`.`description` AS `description`,`getContactRoleList`(`acc`.`id`) AS `role_list`,`getContactCommunicationList`(`cont`.`id`) AS `communication_list`,`getContactFlatsList`(`cont`.`id`) AS `flat_list` from ((`contact` `cont` left join `account` `acc` on((`acc`.`contact_id` = `cont`.`id`))) join `contact_type` `cont_type` on((`cont`.`contact_type_id` = `cont_type`.`id`))))$$

DELIMITER ;