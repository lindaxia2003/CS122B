=========

For CS 122B, 2015 Winter, UCI
By Xinning Shen, Pengyu Ji, Bixia Si and Yi Lin, Group 8

Testing Employee
----------------

Name: root root   
Email: root@root.com 
Password: root 

Setup database
--------------
	
	Please change the username and password of database under META-INF/context.xml

    $ cd /project2_8/WEB-INF/source/
    $ mysql -u root -p

    // There are extra tables in createtable.sql, so please re-create the moviedb database, and reload the data. 
	mysql> source createtable.sql;

    mysql> source data.sql;                     // for project 2 & project 3-1
    mysql> source raw_data.sql;                 // for project 3-2, 3-3

    mysql> source mysql_procedures.sql;         // create add-movie procedure 

    // To execute procedures, database user must have access to the mysql.proc table. 
    // To add this privilege: (You need to change "testuser" to your username of mysql)
    mysql> GRANT SELECT ON mysql.proc to testuser@localhost;


To compile
----------

    Go to /project2_8/WEB-INF/source/
	$ sudo javac -d ../classes/ -cp ../lib/mysql-connector-java-5.1.34-bin.jar:../lib/servlet-api-3.0.jar *.java ./MyClasses/*.java


Keep in Mind
------------

To remove procedure:

    mysql> DROP PROCEDURE add_movie;

To run procedure:

    mysql> CALL add_movie(parameters here);

When writing procedures, can use IN/OUT before specifying parameters. IN is for input. OUT is output.

To debug:

	Exception is printed in catalina.out, you can search it in your computer.
	You may also add "System.out.println();" in *.java, the result will also be printed in catalina.out.
