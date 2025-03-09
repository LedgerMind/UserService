# Використовуємо офіційний образ OpenJDK версії 23
FROM openjdk:23

# Встановлюємо робочу директорію в контейнері
WORKDIR /app

# Копіюємо зібраний JAR файл у контейнер
COPY target/User-Service-0.0.1-SNAPSHOT.jar /app/User-Service-0.0.1-SNAPSHOT.jar

# Встановлюємо команду запуску контейнера для виконання JAR файлу
ENTRYPOINT ["java", "-jar", "/app/User-Service-0.0.1-SNAPSHOT.jar"]

# Вказуємо порт, на якому додаток буде доступний
EXPOSE 8081
