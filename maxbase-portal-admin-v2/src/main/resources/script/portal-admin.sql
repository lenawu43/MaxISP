
SET NAMES utf8mb4;

SET FOREIGN_KEY_CHECKS = 0;-- ----------------------------
-- View structure for mobileNavigation_vo
-- ----------------------------
DROP VIEW
IF
	EXISTS `mobileNavigation_vo`;
CREATE ALGORITHM = UNDEFINED DEFINER = `root` @`%` SQL SECURITY DEFINER VIEW `mobileNavigation_vo` AS SELECT
`mn`.`id` AS `id`,
`mn`.`mobile_navigation_code` AS `mobile_navigation_code`,
`mn`.`mobile_navigation_name` AS `mobile_navigation_name`,
`mn`.`icon_path` AS `icon_path`,
`mn`.`description` AS `description`,
`mn`.`application_code` AS `application_code`,
`mn`.`business_type_id` AS `business_type_id`,
`mn`.`url` AS `url`,
`mn`.`url_scheme` AS `url_scheme`,
`mn`.`order_no` AS `order_no`,
`mn`.`created_by` AS `created_by`,
`mn`.`created_date` AS `created_date`,
`mn`.`last_modified_by` AS `last_modified_by`,
`mn`.`last_modified_date` AS `last_modified_date`,
`bt`.`business_type_name` AS `business_type_name`,
`bt`.`order_no` AS `business_type_order_no`
FROM
	( `mobile_navigation` `mn` LEFT JOIN `business_type` `bt` ON ( ( `mn`.`business_type_id` = `bt`.`id` ) ) )
WHERE
	( `bt`.`platform` = 'mobile' );-- ----------------------------
-- View structure for webNavigation_vo
-- ----------------------------
DROP VIEW
IF
	EXISTS `webNavigation_vo`;
CREATE ALGORITHM = UNDEFINED DEFINER = `root` @`%` SQL SECURITY DEFINER VIEW `webNavigation_vo` AS SELECT
	`wn`.`id` AS `id`,
	`wn`.`web_navigation_code` AS `web_navigation_code`,
	`wn`.`web_navigation_name` AS `web_navigation_name`,
	`wn`.`icon_path` AS `icon_path`,
	`wn`.`description` AS `description`,
	`wn`.`application_code` AS `application_code`,
	`wn`.`business_type_id` AS `business_type_id`,
	`wn`.`web_url` AS `web_url`,
	`wn`.`app_download_url` AS `app_download_url`,
	`wn`.`navigation_type` AS `navigation_type`,
	`wn`.`status` AS `status`,
	`wn`.`order_no` AS `order_no`,
	`wn`.`created_by` AS `created_by`,
	`wn`.`created_date` AS `created_date`,
	`wn`.`last_modified_by` AS `last_modified_by`,
	`wn`.`last_modified_date` AS `last_modified_date`,
	`bt`.`business_type_name` AS `business_type_name`,
	`bt`.`order_no` AS `business_type_order_no`
FROM
	(
	`web_navigation` `wn`
	LEFT JOIN `business_type` `bt` ON ( ( ( `wn`.`business_type_id` = `bt`.`id` ) AND ( `wn`.`navigation_type` IN ( 'application', 'navigation' ) ) ) )
	)
WHERE
	( `bt`.`platform` = 'web' ) UNION
SELECT
	`wn`.`id` AS `id`,
	`wn`.`web_navigation_code` AS `web_navigation_code`,
	`wn`.`web_navigation_name` AS `web_navigation_name`,
	`wn`.`icon_path` AS `icon_path`,
	`wn`.`description` AS `description`,
	( SELECT group_concat( `agi`.`application_code` SEPARATOR ',' ) FROM `application_group_item` `agi` WHERE ( `agi`.`web_navigation_id` = `wn`.`id` ) ) AS `application_code`,
	`wn`.`business_type_id` AS `business_type_id`,
	`wn`.`web_url` AS `web_url`,
	`wn`.`app_download_url` AS `app_download_url`,
	`wn`.`navigation_type` AS `navigation_type`,
	`wn`.`status` AS `status`,
	`wn`.`order_no` AS `order_no`,
	`wn`.`created_by` AS `created_by`,
	`wn`.`created_date` AS `created_date`,
	`wn`.`last_modified_by` AS `last_modified_by`,
	`wn`.`last_modified_date` AS `last_modified_date`,
	`bt`.`business_type_name` AS `business_type_name`,
	`bt`.`order_no` AS `business_type_order_no`
FROM
	(
	`web_navigation` `wn`
	LEFT JOIN `business_type` `bt` ON ( ( ( `wn`.`business_type_id` = `bt`.`id` ) AND ( `wn`.`navigation_type` = 'applicationGroup' ) ) )
	)
WHERE
	( `bt`.`platform` = 'web' );-- ----------------------------


SET FOREIGN_KEY_CHECKS = 1;