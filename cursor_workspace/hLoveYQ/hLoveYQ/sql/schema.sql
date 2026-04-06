-- 护你周全 - MySQL 8+ schema
-- This file is the "source of truth" DDL for current entities:
-- - com.whh.hloveyq.domain.User
-- - com.whh.hloveyq.domain.PeriodRecord

-- Create database (optional)
CREATE DATABASE IF NOT EXISTS auntie_tracker
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_0900_ai_ci;

USE auntie_tracker;

-- ----------------------------
-- users
-- ----------------------------
-- Notes:
-- - email is unique (see uk_users_email)
-- - password_hash stores BCrypt encoded password
CREATE TABLE IF NOT EXISTS users (
  id            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  email         VARCHAR(255)     NOT NULL,
  password_hash VARCHAR(255)     NOT NULL,
  nickname      VARCHAR(50)      NOT NULL,
  created_at    DATETIME(3)      NOT NULL DEFAULT CURRENT_TIMESTAMP(3),

  PRIMARY KEY (id),
  UNIQUE KEY uk_users_email (email),
  KEY idx_users_created_at (created_at)
) ENGINE=InnoDB;

-- ----------------------------
-- period_records
-- ----------------------------
-- Notes:
-- - end_date can be NULL (meaning "ongoing / unknown end")
-- - flow_level: 1..5, pain_level: 0..10 (enforced by app; CHECK is extra hardening)
CREATE TABLE IF NOT EXISTS period_records (
  id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id     BIGINT UNSIGNED NOT NULL,
  start_date  DATE            NOT NULL,
  end_date    DATE            NULL,
  flow_level  TINYINT UNSIGNED NULL,
  pain_level  TINYINT UNSIGNED NULL,
  note        VARCHAR(500)    NULL,
  created_at  DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),

  PRIMARY KEY (id),
  CONSTRAINT fk_period_records_user
    FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE,

  -- Common queries: list recent records & compute cycles
  KEY idx_pr_user_start (user_id, start_date),
  KEY idx_pr_user_created (user_id, created_at),

  -- Extra hardening (MySQL 8.0.16+ enforces CHECK)
  CONSTRAINT chk_pr_end_ge_start CHECK (end_date IS NULL OR end_date >= start_date),
  CONSTRAINT chk_pr_flow_range   CHECK (flow_level IS NULL OR (flow_level BETWEEN 1 AND 5)),
  CONSTRAINT chk_pr_pain_range   CHECK (pain_level IS NULL OR (pain_level BETWEEN 0 AND 10))
) ENGINE=InnoDB;

