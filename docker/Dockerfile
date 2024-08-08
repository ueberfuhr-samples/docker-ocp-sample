FROM eclipse-temurin:17-ubi9-minimal
EXPOSE 8080
ENV CONTEXT_ROOT=/gelbekatze
COPY target/app.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

# docker run --rm -p 8080:8080 -it  -e CONTEXT_ROOT='//roter-panda' petclinic
# optional: -e SPRING_PROFILES_ACTIVE=postgresql
# oder: docker run --rm -p 8080:8080 -it  petclinic --server.servlet.context-path=//test

# oder JAR in /deployments kopieren, dann kann der ENTRYPOINT weggelassen werden (Convention)
