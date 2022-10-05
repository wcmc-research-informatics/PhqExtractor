package listeners

import gov.va.vinci.ef.listeners.BasicDatabaseListener

int batchSize = 1000

String url = "jdbc:mysql://<server>:3306/psychiatric?autoReconnect=true&useSSL=false"
String driver = "com.mysql.jdbc.Driver"
String dbUser = "<usr>"
String dbPwd = "<paswd>"
String dbsName = "psychiatric"
String tableName = "phq_output"

// String url = "jdbc:sqlserver://vits-archsqlp02.med.cornell.edu;databasename=DM_NLP;integratedSecurity=false"
// String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver"
// String dbUser = "usr_dm_nlp"
// String dbPwd = "us3r_83_Nlp"
// String dbsName = "DM_NLP"
// String tableName = "dbo.depression_phq_output"

incomingTypes = "gov.va.vinci.ef.types.Relation"
// incomingTypes = "gov.va.vinci.ef.types.ContextWindow"

fieldList = [
        ["DocId", "0", "varchar(500)"],
        ["Term", "-1", "varchar(500)"],
        ["Value", "-1", "varchar(100)"],
        ["Value2", "-1", "varchar(100)"],
        ["ValueString", "-1", "varchar(100)"],
        ["InstanceID", "-1", "int"],
        ["Snippets", "-1", "varchar(8000)"],
        ["SpanStart", "-1", "int"],
        ["SpanEnd", "-1", "int"]
]

boolean dropExisting = false;
listener = BasicDatabaseListener.createNewListener(
        driver,
        url,
        dbUser,
        dbPwd,
        dbsName,
        tableName,
        batchSize,
        fieldList,
        incomingTypes)

// Comment out the statement below if you want to add to the existing table
listener.createTable(dropExisting);
