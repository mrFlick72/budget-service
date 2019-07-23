FROM openjdk:11

ADD spent-budget-service.jar /usr/local/spent-budget-service/

WORKDIR /usr/local/spent-budget-service/

CMD ["java", "-Xms512m", "-Xmx1g", "-jar", "spent-budget-service.jar"]