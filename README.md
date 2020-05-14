1. Demo video URL: https://drive.google.com/file/d/1cDfSguG4CGMLZMAyXcsrudIbf_FFRmJK/view

2. Deploy Instructions
    1. git clone https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-25.git
    
    Creating database(moviedb) from files in repository
    
    If you do not have USER mytestuser setup in MySQL, follow the below steps to create it:\
    login to mysql as a root user

    local> mysql -u root -p
    
    create a test user and grant privileges:

    mysql> CREATE USER 'mytestuser'@'localhost' IDENTIFIED BY 'mypassword';
    
    mysql> GRANT ALL PRIVILEGES ON * . * TO 'mytestuser'@'localhost';
    
    mysql> quit;
    
    local> mysql -u mytestuser -p
    
    mysql> CREATE DATABASE IF NOT EXISTS moviedb;
    
    mysql> USE moviedb;
    
    mysql> source /path/to/createtable.sql 
    
    mysql> source /path/to/movie-data.sql
    

    2. inside your repo, where the pom.xml file locates, build the war file:
    mvn package
    3. show tomcat web apps, it should NOT have the war file yet:
    ls -lah /path/to/tomcat/webapps
    4. copy your newly built war file:
    cp ./target/*.war /path/to/tomcat/webapps
    5. show tomcat web apps, it should now have the new war file:
    ls -lah /path/to/tomcat/webapps
    6. open the tomcat manager page and click on fablix-pi, which will show the fablix website
    
    

3. Prepared Statements:  
[AddMovieServlet](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-25/blob/master/src/AddMovieServlet.java)  
[AdvancedSearchServlet](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-25/blob/master/src/AdvancedSearchServlet.java)  
[BrowseServlet](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-25/blob/master/src/BrowseServlet.java)  
[CheckoutServlet](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-25/blob/master/src/CheckoutServlet.java)  
[DashboardServlet](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-25/blob/master/src/DashboardServlet.java)  
[EmployeeLoginServlet](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-25/blob/master/src/EmployeeLoginServlet.java)  
[LoginServlet](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-25/blob/master/src/LoginServlet.java)  
[MovieListServlet](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-25/blob/master/src/MovieListServlet.java)  
[NewStarServlet](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-25/blob/master/src/NewStarServlet.java)  
[SearchServlet](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-25/blob/master/src/SearchServlet.java)  
[SingleMovieServlet](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-25/blob/master/src/SingleMovieServlet.java)  
[SingleStarServlet](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-25/blob/master/src/SingleStarServlet.java)  
  
4. Two parsing time optimization strategies
[Report.txt](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-25/blob/master/Report.txt) 
  
5. Contribution:

Jongmin Lee: reCAPTCHA, Dashboard, Add star

Joey Hwang: Add movie, Stored procedure

These are the main parts we split up, but we checked each other's parts and edited them.
