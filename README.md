# Coronavirus Tracker Application

a Java Spring Boot application to track reported data of confirmed Coronavirus infections COVID-19 (2019-nCoV) around the world.

Data source used: Novel Coronavirus (COVID-19) Cases, provided by JHU CSSE 
https://github.com/CSSEGISandData/COVID-19

The project uses SpringBoot, Thymeleaf and bootstrap css to present the data.

## Running application locally
```
git clone https://github.com/viralharia/coronavirus-tracker.git
cd coronavirus-tracker
mvnw spring-boot:run
```

## AWS
I have also deployed the application on AWS using Elastic beanstalk.

Demo url - http://coronavirus-tracker-dev.ap-south-1.elasticbeanstalk.com/

P.S. - if the url is not working, then i may have shutdown the EC2 instances on AWS to save the bill :)

Reference - I followed this awsome tutorial by Kaushik for this (https://www.youtube.com/watch?v=8hjNG9GZGnQ&t=116s)
