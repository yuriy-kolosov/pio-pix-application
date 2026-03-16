-- liquibase formatted sql

-- changeset yuriy_kolosov:1
CREATE TABLE IF NOT EXISTS "user" (
  id bigserial PRIMARY KEY,
  name VARCHAR(500) UNIQUE NOT NULL,
  date_of_birth DATE NOT NULL,
  "password" VARCHAR(500) NOT NULL
);

CREATE TABLE IF NOT EXISTS account (
  id bigserial PRIMARY KEY,
  user_id bigint NOT NULL,
  balance DECIMAL NOT NULL,
  CONSTRAINT account_fk_ref_user_id FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE,
  CONSTRAINT balance_value CHECK (balance >= 0.00)
);

CREATE TABLE IF NOT EXISTS email_data (
  id bigserial PRIMARY KEY,
  user_id bigint NOT NULL,
  email VARCHAR(200) NOT NULL,
  CONSTRAINT email_data_fk_ref_user_id FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS phone_data (
  id bigserial PRIMARY KEY,
  user_id bigint NOT NULL,
  phone VARCHAR(13) NOT NULL,
  CONSTRAINT phone_data_fk_ref_user_id FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS transfer_data (
  id bigserial PRIMARY KEY,
  user_from_id bigint NOT NULL,
  user_to_id bigint NOT NULL,
  amount DECIMAL NOT NULL,
  local_date_time timestamp NOT NULL,
  CONSTRAINT transfer_data_fk_ref_user_from_id FOREIGN KEY (user_from_id) REFERENCES "user" (id) ON DELETE CASCADE,
  CONSTRAINT transfer_data_fk_ref_user_to_id FOREIGN KEY (user_to_id) REFERENCES "user" (id) ON DELETE CASCADE
);

insert into "user" (name, date_of_birth, password) values ('user1', '1993-05-01', '$2a$10$UPozb.WWFzap5Nnc44nin.5VjXPx8.i3RMtGQQEeH4xEB80kdixIS');
insert into "user" (name, date_of_birth, password) values ('user2', '2000-08-07', '$2a$10$MbNRtpuXqLFkfaV48rMeIOIYshsgvEMLiXNLl37Omdsmg/3hpZOjC');
insert into "user" (name, date_of_birth, password) values ('user3', '1985-12-07', '$2a$10$nJDwIYvG8jztWOsc1rKyL.m.ddBt8tREwljwxVd1f2ck2/kAUVh6.');
insert into "user" (name, date_of_birth, password) values ('user4', '1977-01-17', '$2a$10$7Tg3w8HvMWkWEF7k72eBZO6iwVnCc6V/lBiX8w7Gfo2l9sfKLWXWy');
insert into "user" (name, date_of_birth, password) values ('user5', '2002-11-21', '$2a$10$fnIX8fKVsKqpJnuF7ApJdOTNXqBn0nYGO3JpbvwTwtqoQ3rpQnrZi');

insert into account (user_id, balance) values (1, 100);
insert into account (user_id, balance) values (2, 2000);
insert into account (user_id, balance) values (3, 30);
insert into account (user_id, balance) values (4, 4);
insert into account (user_id, balance) values (5, 50000);

insert into email_data (user_id, email) values (1, 'user1@piopix.ru');
insert into email_data (user_id, email) values (2, 'user2@piopix.ru');
insert into email_data (user_id, email) values (3, 'user3@piopix.ru');
insert into email_data (user_id, email) values (4, 'user4@piopix.ru');
insert into email_data (user_id, email) values (5, 'user5@piopix.ru');

insert into phone_data (user_id, phone) values (1, '79107865431');
insert into phone_data (user_id, phone) values (2, '79207865432');
insert into phone_data (user_id, phone) values (3, '79307865433');
insert into phone_data (user_id, phone) values (4, '79407865434');
insert into phone_data (user_id, phone) values (5, '79507865435');


