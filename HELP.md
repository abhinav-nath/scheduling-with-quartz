# How to run the application

1. Bring a Postgres DB up using docker-compose.yml

   ```
   docker-compose up -d
   ```

2. Connect to the DB and create `source_products` and `target_product` DB tables
   
   ```sql
   CREATE TABLE source_products (
       id INTEGER NOT NULL PRIMARY KEY,
       name VARCHAR(50),
       created_at TIMESTAMP,
       modified_at TIMESTAMP
   );
   
   CREATE TABLE target_products (
       id INTEGER NOT NULL PRIMARY KEY,
       name VARCHAR(50),
       created_at TIMESTAMP,
       modified_at TIMESTAMP
   );
   ```

3. Insert data into Source DB

   ```sql
   INSERT INTO source_products VALUES(1, 'rock', now(), now());
   
   INSERT INTO source_products VALUES(2, 'paper', now(), now());
   
   INSERT INTO source_products VALUES(3, 'scissor', now(), now());
   ```

   Functionality:

   * Currently, the Quartz scheduler is configured to run every 10 seconds.
   * So, any changes in the `source_products` table will be picked up by the batch job in every 10 seconds.
   * The batch job will read the `source_products` table, transform product names to uppercase and write in `target_products` table.

4. Select statements

   ```sql
   SELECT * FROM target_products;

   SELECT * FROM batch_job_execution ORDER BY start_time DESC;

   SELECT * FROM batch_job_execution_context ORDER BY start_time DESC;

   SELECT * FROM batch_job_execution_params ORDER BY start_time DESC;

   SELECT * FROM batch_job_instance ORDER BY start_time DESC;

   SELECT * FROM batch_step_execution ORDER BY start_time DESC;

   SELECT * FROM batch_step_execution_context ORDER BY start_time DESC;
   ```

5. Clean Up (Optional)

   ```sql
   TRUNCATE source_products;
   TRUNCATE target_products;
   
   DROP TABLE source_products;
   DROP TABLE target_products;
   ```