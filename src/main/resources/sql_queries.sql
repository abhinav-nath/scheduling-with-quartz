-- create source_products and target_product tables

CREATE TABLE source_products (
    id INTEGER NOT NULL PRIMARY KEY,
    name VARCHAR(50),
    created_at TIMESTAMP,
    modified_at TIMESTAMP
);

CREATE INDEX idx_source_products_created_at
ON source_products(created_at);

CREATE INDEX idx_source_products_modified_at
ON source_products(modified_at);

CREATE TABLE target_products (
    id INTEGER NOT NULL PRIMARY KEY,
    name VARCHAR(50),
    created_at TIMESTAMP,
    modified_at TIMESTAMP
);


-- Insert data into source DB
  
INSERT INTO source_products VALUES(1, 'rock', now(), now());

INSERT INTO source_products VALUES(2, 'paper', now(), now());

INSERT INTO source_products VALUES(3, 'scissor', now(), now());

INSERT INTO source_products VALUES(4, 'pencil', now(), now());

INSERT INTO source_products VALUES(5, 'eraser', now(), now());


SELECT * FROM target_products;


-- Batch Tables

SELECT * FROM batch_job_execution ORDER BY start_time DESC;

SELECT * FROM batch_job_execution_context;

SELECT * FROM batch_job_execution_params;

SELECT * FROM batch_job_instance;

SELECT * FROM batch_step_execution ORDER BY start_time DESC;

SELECT * FROM batch_step_execution_context;


-- Quartz Tables

SELECT * FROM qrtz_blob_triggers;

SELECT * FROM qrtz_calendars;

SELECT * FROM qrtz_cron_triggers;

SELECT * FROM qrtz_fired_triggers;

SELECT * FROM qrtz_job_details;

SELECT * FROM qrtz_locks;

SELECT * FROM qrtz_paused_trigger_grps;

SELECT * FROM qrtz_scheduler_state;

SELECT * FROM qrtz_simple_triggers;

SELECT * FROM qrtz_simprop_triggers;

SELECT * FROM qrtz_triggers;


-- Clean up (optional)

-- Truncate tables

TRUNCATE source_products;
TRUNCATE target_products;

TRUNCATE batch_job_execution;
TRUNCATE batch_job_execution_context;
TRUNCATE batch_job_execution_params;
TRUNCATE batch_job_instance;
TRUNCATE batch_step_execution;
TRUNCATE batch_step_execution_context;

TRUNCATE qrtz_blob_triggers CASCADE;
TRUNCATE qrtz_calendars CASCADE;
TRUNCATE qrtz_cron_triggers CASCADE;
TRUNCATE qrtz_fired_triggers CASCADE;
TRUNCATE qrtz_job_details CASCADE;
TRUNCATE qrtz_locks CASCADE;
TRUNCATE qrtz_paused_trigger_grps CASCADE;
TRUNCATE qrtz_scheduler_state CASCADE;
TRUNCATE qrtz_simple_triggers CASCADE;
TRUNCATE qrtz_simprop_triggers CASCADE;
TRUNCATE qrtz_triggers CASCADE;

-- Drop tables

DROP TABLE source_products;
DROP TABLE target_products;

DROP TABLE batch_job_execution CASCADE;
DROP TABLE batch_job_execution_context;
DROP TABLE batch_job_execution_params;
DROP TABLE batch_job_instance;
DROP TABLE batch_step_execution CASCADE;
DROP TABLE batch_step_execution_context;

DROP TABLE qrtz_blob_triggers CASCADE;
DROP TABLE qrtz_calendars CASCADE;
DROP TABLE qrtz_cron_triggers CASCADE;
DROP TABLE qrtz_fired_triggers CASCADE;
DROP TABLE qrtz_job_details CASCADE;
DROP TABLE qrtz_locks CASCADE;
DROP TABLE qrtz_paused_trigger_grps CASCADE;
DROP TABLE qrtz_scheduler_state CASCADE;
DROP TABLE qrtz_simple_triggers CASCADE;
DROP TABLE qrtz_simprop_triggers CASCADE;
DROP TABLE qrtz_triggers CASCADE;