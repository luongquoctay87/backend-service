```text
 __ __|                  |
    |   _` |  |   |      |   _` | \ \   /  _` |  \ \   /  __ \
    |  (   |  |   |  \   |  (   |  \ \ /  (   |   \ \ /   |   |
   _| \__,_| \__, | \___/  \__,_|   \_/  \__,_| _) \_/   _|  _|
             ____/ Lập Trình Java Từ A-Z
 
   Website: https://tayjava.vn
   Youtube: https://youtube.com/@tayjava 
   TikTok: https://tiktok.com/@tayjava.vn 
```
## Prerequisite
- Cài đặt JDK 17+ nếu chưa thì [cài đặt JDK](https://tayjava.vn/cai-dat-jdk-tren-macos-window-linux-ubuntu/)
- Install Maven 3.5+ nếu chưa thì [cài đặt Maven](https://tayjava.vn/cai-dat-maven-tren-macos-window-linux-ubuntu/)
- Install IntelliJ nếu chưa thì [cài đặt IntelliJ](https://tayjava.vn/cai-dat-intellij-tren-macos-va-window/)

## Technical Stacks
- Java 17
- Spring Boot 3.2.3
- PostgresSQL
- Kafka
- Redis
- Maven 3.5+
- Lombok
- DevTools
- Docker, Docker compose

## Build application
```bash
mvn clean package -P dev|test|uat|prod
```

## Run application
- Maven statement
```bash
./mvnw spring-boot:run
```
- Jar statement
```bash
java -jar target/backend-service.jar
```

- Docker
```bash
docker build -t backend-service .
docker run -d backend-service:latest backend-service
```

## Package application
```bash
docker build -t backend-service .
```

