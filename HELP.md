# How to test the application

* [SQL queries to be used](./src/main/resources/sql_queries.sql)

1. Bring a Postgres DB up using docker-compose.yml

   ```
   docker-compose up -d
   ```

2. Connect to the DB and create `source_products` and `target_product` DB tables.

3. Insert a new record in the `source_products` table, batch job will pick the record (as per the quartz schedule) and write it into `target_products` table.

4. If any record is updated, make sure to set `modified_at` as `now()` in the `source_products` table so that it can be picked by the job and same update is made in the `target_products` table.

5. If any record has to be deleted then set `modfified_at` as `now()` and `is_deleted` as `true`.
   Batch job will pick this record and will delete it from the `target_products` table.