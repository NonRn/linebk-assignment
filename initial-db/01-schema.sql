-- -------------------------------------------------------------
-- Database: assignment
-- -------------------------------------------------------------


CREATE TABLE "account_balances" (
  "account_id" varchar(50) NOT NULL,
  "user_id" varchar(50) DEFAULT NULL,
  "amount" decimal(15,2) DEFAULT NULL,
  "dummy_col_4" varchar(255) DEFAULT NULL,
  PRIMARY KEY ("account_id")
);

CREATE TABLE "account_details" (
  "account_id" varchar(50) NOT NULL,
  "user_id" varchar(50) DEFAULT NULL,
  "color" varchar(10) DEFAULT NULL,
  "is_main_account" boolean DEFAULT NULL,
  "progress" int DEFAULT NULL,
  "dummy_col_5" varchar(255) DEFAULT NULL,
  PRIMARY KEY ("account_id")
);

CREATE TABLE "account_flags" (
  "flag_id" SERIAL NOT NULL,
  "account_id" varchar(50) NOT NULL,
  "user_id" varchar(50) NOT NULL,
  "flag_type" varchar(50) NOT NULL,
  "flag_value" varchar(30) NOT NULL,
  "created_at" timestamp DEFAULT CURRENT_TIMESTAMP,
  "updated_at" timestamp DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY ("flag_id")
);

CREATE TABLE "accounts" (
  "account_id" varchar(50) NOT NULL,
  "user_id" varchar(50) DEFAULT NULL,
  "type" varchar(50) DEFAULT NULL,
  "currency" varchar(10) DEFAULT NULL,
  "account_number" varchar(20) DEFAULT NULL,
  "issuer" varchar(100) DEFAULT NULL,
  "dummy_col_3" varchar(255) DEFAULT NULL,
  PRIMARY KEY ("account_id")
);

CREATE TABLE "banners" (
  "banner_id" varchar(50) NOT NULL,
  "user_id" varchar(50) DEFAULT NULL,
  "title" varchar(255) DEFAULT NULL,
  "description" text,
  "image" varchar(255) DEFAULT NULL,
  "dummy_col_11" varchar(255) DEFAULT NULL,
  PRIMARY KEY ("banner_id")
);

CREATE TABLE "debit_card_design" (
  "card_id" varchar(50) NOT NULL,
  "user_id" varchar(50) DEFAULT NULL,
  "color" varchar(10) DEFAULT NULL,
  "border_color" varchar(10) DEFAULT NULL,
  "dummy_col_9" varchar(255) DEFAULT NULL,
  PRIMARY KEY ("card_id")
);

CREATE TABLE "debit_card_details" (
  "card_id" varchar(50) NOT NULL,
  "user_id" varchar(50) DEFAULT NULL,
  "issuer" varchar(100) DEFAULT NULL,
  "number" varchar(25) DEFAULT NULL,
  "dummy_col_10" varchar(255) DEFAULT NULL,
  PRIMARY KEY ("card_id")
);

CREATE TABLE "debit_card_status" (
  "card_id" varchar(50) NOT NULL,
  "user_id" varchar(50) DEFAULT NULL,
  "status" varchar(20) DEFAULT NULL,
  "dummy_col_8" varchar(255) DEFAULT NULL,
  PRIMARY KEY ("card_id")
);

CREATE TABLE "debit_cards" (
  "card_id" varchar(50) NOT NULL,
  "user_id" varchar(50) DEFAULT NULL,
  "name" varchar(100) DEFAULT NULL,
  "dummy_col_7" varchar(255) DEFAULT NULL,
  PRIMARY KEY ("card_id")
);

CREATE TABLE "transactions" (
  "transaction_id" varchar(50) NOT NULL,
  "user_id" varchar(50) DEFAULT NULL,
  "name" varchar(100) DEFAULT NULL,
  "image" varchar(255) DEFAULT NULL,
  "isBank" boolean DEFAULT NULL,
  "dummy_col_6" varchar(255) DEFAULT NULL,
  PRIMARY KEY ("transaction_id")
);

CREATE TABLE "user_greetings" (
  "user_id" varchar(50) NOT NULL,
  "greeting" text,
  "dummy_col_2" varchar(255) DEFAULT NULL,
  PRIMARY KEY ("user_id")
);

CREATE TABLE "users" (
  "user_id" varchar(50) NOT NULL,
  "name" varchar(100) DEFAULT NULL,
  "dummy_col_1" varchar(255) DEFAULT NULL,
  PRIMARY KEY ("user_id")
);