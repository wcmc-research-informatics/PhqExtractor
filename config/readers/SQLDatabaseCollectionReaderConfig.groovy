package readers

import gov.va.vinci.leo.cr.BatchDatabaseCollectionReader;

String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver"
String url = "jdbc:sqlserver://<database_engine>;databasename=<database_name>;integratedSecurity=false"
String username="<user>";
String password="<password>";
String query = "SELECT ID, NOTE_TEXT FROM <database_name>.<schema>.<table_name> order by ID"


int startingIndex = 0;
int endingIndex = 10;
int batch_size = 10000;

reader = new BatchDatabaseCollectionReader(
        driver,
        url,
        username,
        password,
        query,
        "id","note_text",   /// Make sure that the names of the fields are low case.
        startingIndex , endingIndex
        , batch_size)
