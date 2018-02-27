#!/bin/bash

java -Xmx2048m -Xms512m -Dlog4j.configuration=config/log4j.properties -cp "config/*:lib/*:target/*" gov.va.vinci.ef.Service
