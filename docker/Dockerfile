FROM eclipse-temurin:17-ubi9-minimal
EXPOSE 8080
ENV CONTEXT_ROOT=/pet-clinic
COPY target/spring-petclinic-rest.jar /spring-petclinic-rest.jar
ENTRYPOINT ["java", "-jar", "/spring-petclinic-rest.jar"]
