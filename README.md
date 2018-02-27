## PhqExtractor - Extremely fast document processor


PhqExtractor Pipeline is designed to find Patient Health Questionnaire–9 (PHQ-9) measurements in clinical text. 
PhqExtractor is based on Leo NLP architecture extending UIMA AS. For more info on Leo see [ http://department-of-veterans-affairs.github.io/Leo/userguide.html ]

To use PhqExtractor :

  1.  Follow the instructions to install and configure UIMA-AS on page [ http://department-of-veterans-affairs.github.io/Leo/userguide.html#/a2_Installation_and_Configuration_of_Leo-Example ]  
  
  2. Checkout and install PhqExtractor code base in a directory of choice.
  
  3. Start UIMA AS Broker.
     
  4. Configure PhqExtractor reader and listeners.
    
    4.1 Three readers are available:
     
       4.1.1 FileCollectionReaderConfig.groovy  -- Enter the path to input directory to read simple text files. The files need to have .txt extention. 
      
       4.1.2 BatchDatabaseCollectionReader.groovy -- Enter the database engine, database name, and input query. Update the batch parameters. If you have only one batch, change the ending index to be less than the batch size. If you are using this reader for batch reads, add sequential numbering column called "RowNo" to your input table. The tags {min} and {max} will be automatically replaced with starting and ending RowNo for each batch until edning RowNo reaches the last endingIndex.
       
       4.1.3 SQLDatabaseCollectionReader.groovy -- This is custom reader (BatchDatabaseCollectionReader.groovy) specifically for SQL database.
       
       4.1.4 MySQLDatabaseCollectionReader.groovy -- This is a custom reader (BatchDatabaseCollectionReader.groovy) specifically for MySQL database
       
       4.1.5 SQLServerPagedDatabaseCollectionReaderConfig.groovy - Enter the database engine, database name, and input query. Make sure the input query ends with "order by" clause. The query will be automatically transformed for SQL Server fetching new batch with offset row number. This approach becomes very slow when the number of records reaches over 2.5M records. MS SQL Server queries become very slow at that point.
      
    4.2 Four listeners
    
       4.2.1 SimpleCsvListenerConfig.groovy - Enter the path to the output directory. A new file will be created with a standard output.
      
       4.2.2 SimpleXmiListenerConfig.groocy - Enter the path to the output directory. A new directory with xmi files will be created.
      
       4.2.3 CsvListenerConfig.groovy - this is an example of a custom CSV listener
      
       4.2.4 DatabaseListenerConfig.groovy - this is an example of a custom database listener.
       
       4.2.5 SQLDatabaseListenerConfig.groovy - this is an example of a custom database listener (DatabaseListenerConfig.groovy) for SQL database.
       
       4.2.6 MySQLDatabaseListenerConfig.groovy - this is an example of a custom database listener (DatabaseListenerConfig.groovy) for MySQL database.
           
  5. Use runService.sh or runService.bat script to start the service.
  
  6. Modify runClient.sh or runClient.bat script with  the selected readers and listeners and start the client
