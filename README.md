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
        [Context.xml](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/blob/master/fablix-web/web/META-INF/context.xml)  
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
        Master and Slave data resources are configured in Context.xml.  
        The Pool was set by setting maxTotal, maxIdle, and maxWaitMills inside the Resource.  
        
          
    - #### Explain how Connection Pooling works with two backend SQL:  
        In Context.xml, Master and Slave datasources are defined along with their IP addresses of each instances.  
        Since the connection pooling is set for both data sources, two different backend SQL operations can be performed by making each request connects to the right SQL instance.
        
        
- # Master/Slave  
    - #### Filename/path of all files routing queries to Master/Slave:  
        [Context.xml](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/blob/master/fablix-web/web/META-INF/context.xml)  
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
        
    - #### How read/write requests were routed to Master/Salve SQL:  
        Two DataSources (Master and Slave) are defined in Context.xml.  
        The servlets which have data writing works are set to connect to the Master datasource.  
        The other servlets which have only reading data works are set to connect to the Slave datasource.
         
          
- # JMeter TS/TJ Time Logs  
    - #### Instruction of how to use the 'log_processing.*' script:  
    Move the log_processing script into your tomcat project root folder.  
    
    Run the processing script with the command:  
    python3 log_processing.py  
    (Make sure that you have used the search function of the fablix web application, which makes TJ/TS log files in the tomcat root folder)  
      
    After running the script, it will show the average time of both TJ and TS logs.
    Example output:  
    TS Average = 123456.78  
    TJ Average = 123456.78 
    

        
- # JMeter TS/TJ Time Measurement Report  
  | **Single-instance Version Test Plan**          | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
    |------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
    | Case 1: HTTP/1 thread                          | ![Link](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/blob/master/img/single_case1.png)   | 28                         | 1043974.91                                  | 772841.78                        | ??           |
    | Case 2: HTTP/10 threads                        | ![Link](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/blob/master/img/single_case2.png)   | 28                         | 2530458.66                                  | 1304581.47                        | ??           |
    | Case 3: HTTPS/10 threads                       | ![Link](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/blob/master/img/single_case3.png)   | 39                         | 8103624.07                                  | 5185095.66                       | ??           |
    | Case 4: HTTP/10 threads/No connection pooling  | ![Link](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/blob/master/img/single_case4.png)   | 27                         | 2602723.22                                  | 1332368.04                        | ??           |

    | **Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
    |------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
    | Case 1: HTTP/1 thread                          | ![](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/blob/master/img/scaled_case1.png)   | 27                         | 2338675.77                                  | 1334829.63                        | ??           |
    | Case 2: HTTP/10 threads                        | ![](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/blob/master/img/scaled_case2.png)   | 30                         | 4063917.93                                  | 2232441.62                        | ??           |
    | Case 2: HTTP/10 threads                        | ![](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/blob/master/img/scaled_case3.png)   | 33                         | 5181251.66                                 | 2816168.23                        | ??           |      
