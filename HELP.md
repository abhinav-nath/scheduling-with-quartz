# How to run the application

1. Bring a Postgres DB up using docker-compose.yml

   ```
   docker-compose up -d
   ```

2. Create `source_products` and `target_product` DB tables
   
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
   insert into source_products values(1, 'rock', now(), now());
   
   insert into source_products values(2, 'paper', now(), now());
   
   insert into source_products values(3, 'scissor', now(), now());
   ```

4. Select statements

   ```sql
   select * from target_products;

   select * from batch_job_execution order by start_time desc;

   select * from batch_job_execution_context order by start_time desc;

   select * from batch_job_execution_params order by start_time desc;

   select * from batch_job_instance order by start_time desc;

   select * from batch_step_execution order by start_time desc;

   select * from batch_step_execution_context order by start_time desc;
   ```

5. Clean Up

   ```sql
   truncate source_products;
   
   truncate target_products;
   
   drop table source_products;
   drop table target_products;
   ```