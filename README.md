# Introduction
Java webserver built to serve HTTP requests. <br />
*Personally it is used for me to better understand webservers, HTTP, client-server model.*

# Class Diagram
![java-web-server-diagram](https://github.com/jimmyh3/java-web-server/blob/master/java-web-server-diagram.png)

# Details
As of 9/25/18, this server is capable of serving HTTP response codes: 200, 201, 204, 400, 401, 403, 404, 500. Using designations written in a httpd.conf configuration file, this server can serve dynamic content by using the*ScriptAlias* key to run and deliver script content. Also, each request and response details is written in Common Log Format to a log file designated by the *LogFile* key.