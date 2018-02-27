#!/bin/bash

##java -Xmx1024m -Xms256m -Dlog4j.configuration=file:config/log4j.properties -cp "lib/*:target/*" gov.va.vinci.ef.Client -clientConfigFile "config/ClientConfig.groovy" -readerConfigFile "config/readers/KnowtatorCollectionReaderConfig.groovy" -listenerConfigFile "config/listeners/CsvListenerConfig.groovy" -listenerConfigFile="config/listeners/AuCompareSummaryListenerConfig.groovy"  -listenerConfigFile "config/listeners/"

java -Xmx2048m -Xms512m -Dlog4j.configuration=file:config/log4j.properties -cp "lib/*:target/*" gov.va.vinci.ef.Client -clientConfigFile "config/ClientConfig.groovy" -readerConfigFile "config/readers/MySQLDatabaseCollectionReaderConfig.groovy" -listenerConfigFile "config/listeners/MySQLDatabaseListenerConfig.groovy"