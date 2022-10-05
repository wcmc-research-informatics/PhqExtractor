package listeners

import gov.va.vinci.ef.listeners.BasicDatabaseListener

int batchSize = 1000

String url = "jdbc:sqlserver://<server>;databasename=<db>;integratedSecurity=false"
String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver"
String dbUser = "<usr>"
String dbPwd = "<paswd>"
String dbsName = "<db>"
String tableName = "<table>"

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
