FROM openjdk:12

ADD budget-service.jar /usr/local/budget-service/

WORKDIR /usr/local/budget-service/

CMD ["java", "-Xms512m", "-Xmx1g", "-jar", "budget-service.jar"]