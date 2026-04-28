FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY . .

RUN chmod +x gradlew

EXPOSE 8080
CMD ["./gradlew", "bootRun"]
