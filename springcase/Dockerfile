# Set the base image
FROM openjdk:17-oracle
WORKDIR /app
# Copy the jar file to the container
COPY target/springmidas-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

##Image was built succesfully however there was an error
##when application container was connecting to the postgresql
##Therefore I was not able to check if the dockerized application