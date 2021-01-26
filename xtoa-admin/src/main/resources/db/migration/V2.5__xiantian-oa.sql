ALTER TABLE `xt_oa`.`t_project_plan_task`
MODIFY COLUMN `project_plan_id` bigint(20) NULL COMMENT '计划id' AFTER `project_id`;
ALTER TABLE `xt_oa`.`t_hr_emp`
MODIFY COLUMN `emp_num` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工号' AFTER `user_id`;