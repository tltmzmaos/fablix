- # General  
    - #### Team# : TEAM-191  
      
    - #### Project 5 Video Demo Link:  
      
    - #### Instruction of deployment:
    1. git clone https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191.git
    
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
    
    - #### Collaborations and Work Distribution:  
    Jongmin Lee: Connection Pooling, Master/slave, Loadbalancing
    
    - # Connection Pooling  
        - #### Filename/path of all file using JDBC Connection Pooling:  
        [web.xml](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/tree/master/fablix-web/web/WEB-INF)  
        [AddMovieServlet.java](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/blob/master/fablix-web/src/AddMovieServlet.java)  
        [AdvancedSearchServlet.java](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/blob/master/fablix-web/src/AdvancedSearchServlet.java)  
        [BrowseServlet.java](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/blob/master/fablix-web/src/BrowseServlet.java)  
        [CheckoutServlet.java](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/blob/master/fablix-web/src/CheckoutServlet.java)  
        [LoginServlet.java](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/blob/master/fablix-web/src/LoginServlet.java)  
        [MovieListServlet.java](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/blob/master/fablix-web/src/MovieListServlet.java)  
        [MovieSuggestionServlet.java](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/blob/master/fablix-web/src/MovieSuggestionServlet.java)  
        [NewStarServlet.java](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/blob/master/fablix-web/src/NewStarServlet.java)  
        [SearchServlet.java](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/blob/master/fablix-web/src/SearchServlet.java)  
        [SingleMovieServlet.java](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/blob/master/fablix-web/src/SingleMovieServlet.java)  
        [SingleStarServlet.java](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/blob/master/fablix-web/src/SingleStarServlet.java)  
        
        
        - #### Explain how the Connection Pooling is utilized in the Fabflix code:  
        
        - #### Explain how Connection Pooling works with two backend SQL:  
        
    - # Master/Slave  
        - #### Filename/path of all files routing queries to Master/Slave:  
        
        - #### How read/write requests were routed to Master/Salve SQL:  
        
    
