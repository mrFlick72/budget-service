CREATE TABLE budget_expense
(
  id     VARCHAR (255)    PRIMARY KEY,
  date   DATE           NOT NULL,
  user_name VARCHAR(255) NOT NULL,
  amount DECIMAL(20, 2) NOT NULL,
  note   VARCHAR(255)   NOT NULL,
  tag    VARCHAR(255)   NOT NULL
);

CREATE TABLE BUDGET_EXPENSE_ATTACHMENTS
(
  BUDGET_EXPENSE_ID varchar(255) not null,
  ATTACHMENT_NAME varchar(255) not null,

    primary key (budget_expense_id, attachment_name)
);

CREATE TABLE BUDGET_REVENUE(
  ID VARCHAR(255) PRIMARY KEY           ,
  USER_NAME VARCHAR(255) NOT NULL          ,
  REGISTRATION_DATE DATE  NOT NULL                      ,
  AMOUNT DECIMAL(20, 2) NOT NULL           ,
  NOTE VARCHAR(255) NOT NULL
);

CREATE TABLE SEARCH_TAG(
  key VARCHAR(255) PRIMARY KEY           ,
  value VARCHAR(255) NOT NULL
);