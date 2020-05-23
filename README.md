- Demo video URL: https://drive.google.com/file/d/1gsnCY2ZPS-uDZHLMPFrLr1PVlWyW_aOo/view?usp=sharing

- Deploy Instructions
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
    
    

- Fuzzy Search implementation  
  
"OR" clause is added to the FullText search query to find cases where the return value of ed function is less than or equal to 2.  
SELECT id FROM movies WHERE MATCH (title) AGAINST ("Search Input" IN BOOLEAN MODE) OR ed(title, ?) <= 2;  
  
When the return value of the ed function was set to less than or eqaul to 1, there was not much difference in the search result with the given data. When it was set to more than 3, the search result was too different.  
   
- Contribution:

Jongmin Lee: Full-text Search, Autocomplete, Android, Fuzzy Search
