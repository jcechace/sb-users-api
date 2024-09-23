# > Builder builds the application from source code
#   Setting up cache with buildkit would be useful, however it's omitted for simplicity
FROM eclipse-temurin:21-jdk-alpine as builder
WORKDIR /application
ARG PROJECT_ROOT=../..
COPY ${PROJECT_ROOT}/src src
COPY ${PROJECT_ROOT}/pom.xml ./
COPY ${PROJECT_ROOT}/.mvn .mvn
COPY ${PROJECT_ROOT}/mvnw ./
RUN ./mvnw clean package -Dmaven.test.skip=true

# > Optimizer builds the optimized image by utilizing layered jar
FROM eclipse-temurin:21-jdk-alpine as optimizer
WORKDIR /application
COPY --from=builder /application/target/*.jar application.jar
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

# > Final image is optimized for runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /application
COPY --from=optimizer /application/extracted/dependencies/ ./
COPY --from=optimizer /application/extracted/spring-boot-loader/ ./
COPY --from=optimizer /application/extracted/snapshot-dependencies/ ./
COPY --from=optimizer /application/extracted/application/ ./
ENTRYPOINT ["java", "-jar", "application.jar"]

