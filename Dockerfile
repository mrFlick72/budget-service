FROM mrflick72/graalvm-jdk:java17-22.2.0

ADD target/budget-service /usr/local/budget-service/

VOLUME /var/log/onlyone-portal

WORKDIR /usr/local/budget-service/

CMD ["java", "-jar", "./budget-service"]