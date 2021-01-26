ALTER TABLE `xt_oa`.`t_project_plan_task`
ADD COLUMN `introducer_id` bigint(20) NULL COMMENT '提出人' AFTER `emp_id`;
ALTER TABLE `xt_oa`.`t_project_plan_task`
MODIFY COLUMN `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题' AFTER `project_plan_id`,
DROP INDEX `name`;