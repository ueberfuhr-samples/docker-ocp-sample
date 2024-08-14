FROM postgres:16.4
COPY src/main/resources/db/postgresql/initDB.sql docker-entrypoint-initdb.d/1-schema.sql
COPY src/main/resources/db/postgresql/populateDB.sql docker-entrypoint-initdb.d/2-data.sql
