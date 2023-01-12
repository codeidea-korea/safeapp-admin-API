DROP TABLE IF EXISTS `invite_history`;

CREATE TABLE `invite_history` (
	`id`	bigint	NOT NULL,
	`group_id`	bigint	NULL,
	`group_name`	varchar(50)	NULL,
	`contents`	text	NULL,
	`user_mail`	varchar(50)	NULL,
	`created_at`	datetime	NULL,
	`efective_end_at`	datetime	NULL,
	`url_data`	varchar(100)	NULL
);

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
	`id`	bigint	NOT NULL,
	`type`	int	NULL,
	`sns_allowed`	int	NOT NULL	DEFAULT 0,
	`user_id`	varchar(100)	NOT NULL,
	`phone_no`	varchar(13)	NULL,
	`user_name`	varchar(50)	NULL,
	`email`	varchar(50)	NULL,
	`marketing_allowed`	int	NOT NULL	DEFAULT 0,
	`marketing_allowed_at`	datetime	NULL,
	`message_allowed`	int	NOT NULL	DEFAULT 0,
	`message_allowed_at`	datetime	NULL,
	`created_at`	datetime	NULL,
	`image`	varchar(100)	NULL,
	`password`	varchar(512)	NOT NULL,
	`deleted`	int	DEFAULT 0
);

DROP TABLE IF EXISTS `auth`;

CREATE TABLE `auth` (
	`id`	bigint	NOT NULL,
	`name`	varchar(50)	NULL,
	`allowed_menu`	text	NULL
);

DROP TABLE IF EXISTS `user_auth`;

CREATE TABLE `user_auth` (
	`id`	bigint	NOT NULL,
	`auth_id`	bigint	NOT NULL,
	`user_id`	bigint	NOT NULL,
	`efective_start_at`	datetime	NULL,
	`efective_end_at`	datetime	NULL,
	`created_at`	datetime	NULL,
	`payment_what`	int	NULL,
	`price`	int(10)	NULL
);

DROP TABLE IF EXISTS `project_group`;

CREATE TABLE `project_group` (
	`id`	bigint	NOT NULL,
	`name`	varchar(50)	NULL,
	`project_id`	bigint	NOT NULL,
	`auth_id`	bigint	NOT NULL,
	`user_id`	bigint	NOT NULL
);

DROP TABLE IF EXISTS `project`;

CREATE TABLE `project` (
	`id`	bigint	NOT NULL,
	`name`	varchar(50)	NULL,
	`start_at`	datetime	NULL,
	`end_at`	datetime	NULL,
	`max_user_count`	int	NOT NULL	DEFAULT 0,
	`address`	varchar(200)	NULL,
	`address_detail`	varchar(100)	NULL,
	`contents`	text	NULL,
	`image`	varchar(100)	NULL,
	`status`	int	NOT NULL	DEFAULT 0,
	`created_at`	datetime	NULL
);

DROP TABLE IF EXISTS `project_managers`;

CREATE TABLE `project_managers` (
	`id`	bigint	NOT NULL,
	`project_id`	bigint	NOT NULL,
	`user_id`	bigint	NOT NULL
);

DROP TABLE IF EXISTS `messages`;

CREATE TABLE `messages` (
	`id`	bigint	NOT NULL,
	`user_id`	bigint	NOT NULL,
	`title`	varchar(100)	NULL,
	`contents`	text	NULL,
	`viewed`	int	NOT NULL	DEFAULT 0,
	`deleted`	int	NOT NULL	DEFAULT 0,
	`created_at`	datetime	NULL
);

DROP TABLE IF EXISTS `checklist_template`;

CREATE TABLE `checklist_template` (
	`id`	bigint	NOT NULL,
	`project_id`	bigint	NOT NULL,
	`user_id`	bigint	NOT NULL,
	`name`	varchar(50)	NULL,
	`created_at`	datetime	NULL,
	`updated_at`	datetime	NULL,
	`views`	int	NOT NULL	DEFAULT 0,
	`likes`	int	NOT NULL	DEFAULT 0,
	`tag`	varchar(500)	NULL,
	`related_acid_no`	varchar(50)	NULL,
	`checker_id`	bigint	NOT NULL,
	`reviewer_id`	bigint	NOT NULL,
	`approver_id`	bigint	NOT NULL
);

DROP TABLE IF EXISTS `checklist_template_detail`;

CREATE TABLE `checklist_template_detail` (
	`id`	bigint	NOT NULL,
	`template_id`	bigint	NOT NULL,
	`depth`	int	NOT NULL	DEFAULT 0,
	`is_depth`	int	NOT NULL	DEFAULT 0,
	`parent_depth`	int	NULL	DEFAULT 0,
	`contents`	text	NULL,
	`orders`	int	NOT NULL	DEFAULT 0,
	`types`	varchar(500)	NULL
);

DROP TABLE IF EXISTS `checklist_project`;

CREATE TABLE `checklist_project` (
	`id`	bigint	NOT NULL,
	`project_id`	bigint	NOT NULL,
	`user_id`	bigint	NOT NULL,
	`name`	varchar(50)	NULL,
	`created_at`	datetime	NULL,
	`updated_at`	datetime	NULL,
	`views`	int	NOT NULL	DEFAULT 0,
	`likes`	int	NOT NULL	DEFAULT 0,
	`tag`	varchar(500)	NULL,
	`related_acid_no`	varchar(50)	NULL,
	`checker_id`	bigint	NOT NULL,
	`reviewer_id`	bigint	NOT NULL,
	`approver_id`	bigint	NOT NULL,
	`template_id`	bigint	NOT NULL,
	`recheck_reason`	text	NULL,
	`check_at`	datetime	NULL
);

DROP TABLE IF EXISTS `checklist_project_detail`;

CREATE TABLE `checklist_project_detail` (
	`id`	bigint	NOT NULL,
	`depth`	int	NOT NULL	DEFAULT 0,
	`is_depth`	int	NOT NULL	DEFAULT 0,
	`parent_depth`	int	NULL	DEFAULT 0,
	`contents`	text	NULL,
	`orders`	int	NOT NULL	DEFAULT 0,
	`types`	varchar(500)	NULL,
	`checklist_id`	bigint	NOT NULL
);

DROP TABLE IF EXISTS `checklist_project_result`;

CREATE TABLE `checklist_project_result` (
	`id`	bigint	NOT NULL,
	`detail_id`	bigint	NOT NULL,
	`check`	int	NOT NULL	DEFAULT 0,
	`memo`	text	NULL,
	`status`	int	NOT NULL	DEFAULT 0
);

DROP TABLE IF EXISTS `checklist_project_result_img`;

CREATE TABLE `checklist_project_result_img` (
	`id`	bigint	NOT NULL,
	`result_id`	bigint	NOT NULL,
	`url`	varchar(100)	NULL
);

DROP TABLE IF EXISTS `risk_template`;

CREATE TABLE `risk_template` (
	`id`	bigint	NOT NULL,
	`project_id`	bigint	NOT NULL,
	`user_id`	bigint	NOT NULL,
	`name`	varchar(50)	NULL,
	`created_at`	datetime	NULL,
	`updated_at`	datetime	NULL,
	`views`	int	NOT NULL	DEFAULT 0,
	`likes`	int	NOT NULL	DEFAULT 0,
	`tag`	varchar(500)	NULL,
	`related_acid_no`	varchar(50)	NULL,
	`checker_id`	bigint	NOT NULL,
	`reviewer_id`	bigint	NOT NULL,
	`approver_id`	bigint	NOT NULL,
	`visibled`	int	NOT NULL	DEFAULT 0,
	`instruct_work`	text	NULL,
	`instruct_detail`	text	NULL,
	`work_start_at`	datetime	NULL,
	`work_end_at`	datetime	NULL,
	`etc_risk_memo`	text	NULL,
	`due_user_id`	bigint	NOT NULL,
	`check_user_id`	bigint	NOT NULL
);

DROP TABLE IF EXISTS `risk_template_detail`;

CREATE TABLE `risk_template_detail` (
	`id`	bigint	NOT NULL,
	`risk_temp_id`	bigint	NOT NULL,
	`contents`	text	NULL,
	`address`	varchar(200)	NULL,
	`address_detail`	varchar(100)	NULL,
	`tools`	varchar(500)	NULL,
	`risk_factor_type`	varchar(200)	NULL,
	`relate_law`	varchar(200)	NULL,
	`relate_guide`	varchar(200)	NULL,
	`risk_type`	int	NOT NULL	DEFAULT 0,
	`orders`	int	NULL	DEFAULT 0,
	`parent_depth`	int	NULL	DEFAULT 0,
	`parent_orders`	int	NULL	DEFAULT 0,
	`reduce_response`	text	NULL,
	`check_memo`	text	NULL,
	`due_user_id`	bigint	NOT NULL,
	`check_user_id`	bigint	NOT NULL
);

DROP TABLE IF EXISTS `risk_check`;

CREATE TABLE `risk_check` (
	`id`	bigint	NOT NULL,
	`project_id`	bigint	NOT NULL,
	`user_id`	bigint	NOT NULL,
	`name`	varchar(50)	NULL,
	`created_at`	datetime	NULL,
	`updated_at`	datetime	NULL,
	`views`	int	NOT NULL	DEFAULT 0,
	`likes`	int	NOT NULL	DEFAULT 0,
	`tag`	varchar(500)	NULL,
	`related_acid_no`	varchar(50)	NULL,
	`checker_id`	bigint	NOT NULL,
	`reviewer_id`	bigint	NOT NULL,
	`approver_id`	bigint	NOT NULL,
	`visibled`	int	NOT NULL	DEFAULT 0,
	`instruct_work`	text	NULL,
	`instruct_detail`	text	NULL,
	`work_start_at`	datetime	NULL,
	`work_end_at`	datetime	NULL,
	`etc_risk_memo`	text	NULL,
	`due_user_id`	bigint	NOT NULL,
	`check_user_id`	bigint	NOT NULL,
	`status`	varchar(50)	NULL	DEFAULT 0,
	`recheck_reason`	text	NULL
);

DROP TABLE IF EXISTS `risk_check_detail`;

CREATE TABLE `risk_check_detail` (
	`id`	bigint	NOT NULL,
	`contents`	text	NULL,
	`address`	varchar(200)	NULL,
	`address_detail`	varchar(100)	NULL,
	`tools`	varchar(500)	NULL,
	`risk_factor_type`	varchar(200)	NULL,
	`relate_law`	varchar(200)	NULL,
	`relate_guide`	varchar(200)	NULL,
	`risk_type`	int	NOT NULL	DEFAULT 0,
	`reduce_response`	text	NULL,
	`check_memo`	text	NULL,
	`due_user_id`	bigint	NOT NULL,
	`check_user_id`	bigint	NOT NULL,
	`risk_check_id`	bigint	NOT NULL,
	`orders`	int	NULL	DEFAULT 0,
	`parent_depth`	int	NULL	DEFAULT 0,
	`parent_orders`	int	NULL	DEFAULT 0,
	`status`	int	NOT NULL	DEFAULT 0
);

DROP TABLE IF EXISTS `accident_exp`;

CREATE TABLE `accident_exp` (
	`id`	bigint	NOT NULL,
	`title`	varchar(100)	NULL,
	`user_id`	bigint	NOT NULL,
	`created_at`	datetime	NULL,
	`views`	int	NULL	DEFAULT 0,
	`image`	varchar(100)	NULL,
	`tags`	varchar(500)	NULL,
	`name`	varchar(50)	NULL,
	`accident_at`	datetime	NULL,
	`accident_uid`	varchar(100)	NULL,
	`accident_reason`	text	NULL,
	`accident_cause`	text	NULL,
	`cause_detail`	text	NULL,
	`response`	text	NULL
);

DROP TABLE IF EXISTS `concern_accident_exp`;

CREATE TABLE `concern_accident_exp` (
	`id`	bigint	NOT NULL,
	`title`	varchar(100)	NULL,
	`user_id`	bigint	NOT NULL,
	`created_at`	datetime	NULL,
	`views`	int	NULL	DEFAULT 0,
	`image`	varchar(100)	NULL,
	`tags`	varchar(500)	NULL,
	`name`	varchar(50)	NULL,
	`accident_user_name`	varchar(50)	NULL,
	`accident_type`	int	NOT NULL	DEFAULT 0,
	`accident_at`	datetime	NULL,
	`accident_reason`	text	NULL,
	`accident_cause`	text	NULL,
	`cause_detail`	text	NULL,
	`response`	text	NULL
);

DROP TABLE IF EXISTS `notice`;

CREATE TABLE `notice` (
	`id`	bigint	NOT NULL,
	`type`	int	NOT NULL	DEFAULT 0,
	`title`	varchar(100)	NOT NULL,
	`contents`	text	NOT NULL,
	`created_at`	datetime	NULL,
	`user_id`	bigint	NOT NULL
);

DROP TABLE IF EXISTS `inquiry`;

CREATE TABLE `inquiry` (
	`id`	bigint	NOT NULL,
	`service_name`	varchar(50)	NOT NULL,
	`title`	varchar(100)	NOT NULL,
	`contents`	text	NOT NULL,
	`answer`	text	NULL,
	`created_at`	datetime	NULL,
	`is_answer`	int	NOT NULL	DEFAULT 0,
	`attachment`	varchar(100)	NULL,
	`answer_user_id`	bigint	NOT NULL
);

ALTER TABLE `invite_history` ADD CONSTRAINT `PK_INVITE_HISTORY` PRIMARY KEY (
	`id`
);

ALTER TABLE `users` ADD CONSTRAINT `PK_USERS` PRIMARY KEY (
	`id`
);

ALTER TABLE `auth` ADD CONSTRAINT `PK_AUTH` PRIMARY KEY (
	`id`
);

ALTER TABLE `user_auth` ADD CONSTRAINT `PK_USER_AUTH` PRIMARY KEY (
	`id`
);

ALTER TABLE `project_group` ADD CONSTRAINT `PK_PROJECT_GROUP` PRIMARY KEY (
	`id`
);

ALTER TABLE `project` ADD CONSTRAINT `PK_PROJECT` PRIMARY KEY (
	`id`
);

ALTER TABLE `project_managers` ADD CONSTRAINT `PK_PROJECT_MANAGERS` PRIMARY KEY (
	`id`
);

ALTER TABLE `messages` ADD CONSTRAINT `PK_MESSAGES` PRIMARY KEY (
	`id`
);

ALTER TABLE `checklist_template` ADD CONSTRAINT `PK_CHECKLIST_TEMPLATE` PRIMARY KEY (
	`id`
);

ALTER TABLE `checklist_template_detail` ADD CONSTRAINT `PK_CHECKLIST_TEMPLATE_DETAIL` PRIMARY KEY (
	`id`
);

ALTER TABLE `checklist_project` ADD CONSTRAINT `PK_CHECKLIST_PROJECT` PRIMARY KEY (
	`id`
);

ALTER TABLE `checklist_project_detail` ADD CONSTRAINT `PK_CHECKLIST_PROJECT_DETAIL` PRIMARY KEY (
	`id`
);

ALTER TABLE `checklist_project_result` ADD CONSTRAINT `PK_CHECKLIST_PROJECT_RESULT` PRIMARY KEY (
	`id`
);

ALTER TABLE `checklist_project_result_img` ADD CONSTRAINT `PK_CHECKLIST_PROJECT_RESULT_IMG` PRIMARY KEY (
	`id`
);

ALTER TABLE `risk_template` ADD CONSTRAINT `PK_RISK_TEMPLATE` PRIMARY KEY (
	`id`
);

ALTER TABLE `risk_template_detail` ADD CONSTRAINT `PK_RISK_TEMPLATE_DETAIL` PRIMARY KEY (
	`id`
);

ALTER TABLE `risk_check` ADD CONSTRAINT `PK_RISK_CHECK` PRIMARY KEY (
	`id`
);

ALTER TABLE `risk_check_detail` ADD CONSTRAINT `PK_RISK_CHECK_DETAIL` PRIMARY KEY (
	`id`
);

ALTER TABLE `accident_exp` ADD CONSTRAINT `PK_ACCIDENT_EXP` PRIMARY KEY (
	`id`
);

ALTER TABLE `concern_accident_exp` ADD CONSTRAINT `PK_CONCERN_ACCIDENT_EXP` PRIMARY KEY (
	`id`
);

ALTER TABLE `notice` ADD CONSTRAINT `PK_NOTICE` PRIMARY KEY (
	`id`
);

ALTER TABLE `inquiry` ADD CONSTRAINT `PK_INQUIRY` PRIMARY KEY (
	`id`
);


DROP TABLE IF EXISTS `if_payment`;

CREATE TABLE `if_payment` (
	`id`	bigint	NOT NULL,
	`imp_uid`	varchar(100)	NULL,
	`merchant_uid`	varchar(100)	NULL,
	`pay_method`	varchar(100)	NULL,
	`channel`	varchar(100)	NULL,
	`pg_provider`	varchar(100)	NULL,
	`emb_pg_provider`	varchar(100)	NULL,
	`pg_tid`	varchar(100)	NULL,
	`pg_id`	varchar(100)	NULL,
	`apply_num`	varchar(100)	NULL,
	`bank_code`	varchar(10)	NULL,
	`bank_name`	varchar(20)	NULL,
	`card_code`	varchar(30)	NULL,
	`card_name`	varchar(100)	NULL,
	`card_number`	varchar(40)	NULL,
	`card_type`	varchar(100)	NULL,
	`vbank_code`	varchar(100)	NULL,
	`vbank_name`	varchar(100)	NULL,
	`vbank_num`	varchar(100)	NULL,
	`vbank_holder`	varchar(100)	NULL,
	`vbank_date`	int	default 0,
	`vbank_issued_at`	int	default 0,
	`card_quota`	int	default 0,
	`name`	varchar(100)	NULL,
	`amount`	int	default 0,
	`cancel_amount`	int	default 0,
	`currency`	varchar(100)	NULL,

	`buyer_name`	varchar(100)	NULL,
	`buyer_email`	varchar(100)	NULL,
	`buyer_tel`	varchar(20)	NULL,
	`buyer_addr`	varchar(100)	NULL,
	`buyer_postcode`	varchar(10)	NOT NULL,

	`custom_data`	varchar(100)	NOT NULL,
	`user_agent`	varchar(200)	NOT NULL,
	`status`	varchar(100)	NOT NULL,

	`started_at`	int	default 0,
	`paid_at`	int	default 0,
	`failed_at`	int	default 0,

	`fail_reason`	varchar(100)	NOT NULL,
	`cancel_reason`	varchar(100)	NOT NULL,
	`receipt_url`	varchar(100)	NOT NULL,
	`cancelled_at`	varchar(100)	NOT NULL,
	`customer_uid`	varchar(100)	NOT NULL,

	`escrow`	int	default 0,
	`cash_receipt_issued`	int	default 0
);
ALTER TABLE `if_payment` ADD CONSTRAINT `PK_if_payment` PRIMARY KEY (
	`id`
);

DROP TABLE IF EXISTS `sms_auth_history`;

CREATE TABLE `sms_auth_history` (
	`id`	bigint	NOT NULL,
	`phone_no`	varchar(15)	NOT NULL,
	`auth_code`	varchar(6)	NOT NULL,
	`efected_ended_at`	datetime	NULL,
	`created_at`	datetime	NULL
);

ALTER TABLE `sms_auth_history` ADD CONSTRAINT `PK_sms_auth_history` PRIMARY KEY (
	`id`
);
