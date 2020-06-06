- # General  
    - #### Team# : TEAM-191  
      
    - #### Project 5 Video Demo Link:  https://drive.google.com/file/d/1YvBj6rlYI9c42DVRluPwPxFUhhz2N1MC/view?usp=sharing  
    (Note: I moved log_processing.* script into the fablix tomcat root folder instead of scp the log files into the local storage)  
      
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
    Jongmin Lee: Connection Pooling, Master/slave, Loadbalancing, Log Processing Script, JMeter report  
    
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
        The Pool was set by setting maxTotal, maxIdle, maxWaitMills, and autoReconnect=true&amp;useSSL=false&amp;cachePrepStmts=true inside the Resource.  
        
          
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
        Since the professor said that it is okay to do hardcode for this project, I used hardcode to route the right SQL.  
         
          
- # JMeter TS/TJ Time Logs  
    - #### Instruction of how to use the 'log_processing.*' script:  
    Copy the log_processing script into your tomcat project root folder.  
    cp log_processing.py ~/tomcat/webapps/fablix-pj   
      
    or  
      
    Scp your log files into the local directory, where the log_processing script is:  
    scp -i "your AWS pem file" ubuntu@"your AWS public address" "Directory where the script is"  
    
    Run the processing script in the fablix tomcat root folder with the command:  
    python3 log_processing.py  
    (Make sure that you have used the search function of the fablix web application, which makes TJ/TS log files in the tomcat root folder)  
      
    After running the script, it will show the average time of both TJ and TS logs.  
    Example output:  
    TS Average = 1.23  
    TJ Average = 1.23 
    

        
- # JMeter TS/TJ Time Measurement Report  
  | **Single-instance Version Test Plan**          | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
    |------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
    | Case 1: HTTP/1 thread                          | ![Link](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/blob/master/img/single_case1.png)   | 32                         | 2.18                                  | 1.69                       | This case shows the fastest performance because there are no other connections to share the core of the computer.           |
    | Case 2: HTTP/10 threads                        | ![Link](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/blob/master/img/single_case2.png)   | 32                         | 4.93                                  | 3.26                        | This case is slower than the Case 1. Due to the nature of the TCP connection, the network speed may decrease as the number of users increases. However, the average query time is about the same as Case 1.           |
    | Case 3: HTTPS/10 threads                       | ![Link](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/blob/master/img/single_case3.png)   | 43                         | 9.86                                  | 6.66                    | HTTPS is always slower than HTTP in the same environment because it has encryption/decryption processes.           |
    | Case 4: HTTP/10 threads/No connection pooling  | ![Link](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/blob/master/img/single_case4.png)   | 34                         | 8.59                                  | 5.51                        | HTTP with no connection pooling is a little slower than HTTP with connection pooling because creating new connections for every request takes longer.          |

    | **Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
    |------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
    | Case 1: HTTP/1 thread                          | ![](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/blob/master/img/scaled_case1.png)   | 32                         | 3.93                                 | 2.61                       | As the single instance test shows, a single thread is faster than multiple threads in the scaled version.           |
    | Case 2: HTTP/10 threads                        | ![](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/blob/master/img/scaled_case2.png)   | 56                         | 5.65                                 | 4.29                        | The case2 shows slower performance than case1. Due to the nature of the TCP connection, the network speed may decrease as the number of users increases.            |
    | Case 3: HTTP/10 threads/No connection pooling                        | ![](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-191/blob/master/img/scaled_case3.png)   | 37                         | 9.59                                 | 6.22                        | This case is the slowest one in the Scaled version test. Since the program has to create a connection for every request, it takes longer to finish the works of search servlets and JDBC connections.           |        
      
    * Results may vary depending on the status of AWS instances. The existing Fabflix AWS instance was used for single instance HTTPS case, and the rest were tested on new AWS instances (load balancer, master, slave, GCP load balancer)
