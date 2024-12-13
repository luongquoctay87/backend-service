#FROM public.ecr.aws/docker/library/openjdk:17
FROM openjdk:17-alpine

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} backend-service.jar

ENTRYPOINT ["java", "-jar", "backend-service.jar"]

EXPOSE 8080