FROM openjdk:17-oracle

WORKDIR /app

COPY target/techchallenge-pagamento-1.0.0-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
