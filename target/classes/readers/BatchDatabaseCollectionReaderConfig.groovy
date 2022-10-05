package readers

import gov.va.vinci.leo.cr.BatchDatabaseCollectionReader;

String driver = "com.mysql.jdbc.Driver"
String url = "jdbc:mysql://<server>:3306/<db>?autoReconnect=true&useSSL=false"
String username="<usr>";
String password="<pwd>";
String query = "SELECT ID, NOTE_TEXT FROM psychiatric.phq_train where id = 1"
String query = "SELECT ID, NOTE_TEXT FROM psychiatric.phq_test"
String query = "SELECT ID, NOTE_TEXT FROM psychiatric.test_notes where id in (6)"
String query = "SELECT ID, NOTE_TEXT FROM psychiatric.test_notes"


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
