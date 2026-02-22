FROM eclipse-temurin:21-jdk

WORKDIR /app
COPY . .

RUN javac -cp .:lib/* -d . src/*.java

EXPOSE 8080

CMD ["java", "-cp", ".:lib/*", "Server"]
