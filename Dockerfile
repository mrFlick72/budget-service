FROM openjdk:11

ADD target/budget-service.jar /usr/local/budget-service/

VOLUME /var/log/onlyone-portal

WORKDIR /usr/local/budget-service/

CMD ["java", "-jar", "budget-service.jar"]