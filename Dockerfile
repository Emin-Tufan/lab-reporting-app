FROM openjdk:17

WORKDIR /app

COPY ./src/main/resources/labReportImage.jpg /app/src/main/resources/

COPY ./src/main/resources/labreport2.png /app/src/main/resources/

COPY ./target/lab-reporting-app-0.0.1-SNAPSHOT.jar /app/app.jar

CMD ["java", "-jar", "/app/app.jar"]

