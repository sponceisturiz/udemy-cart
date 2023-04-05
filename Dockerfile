FROM eclipse-temurin:17.0.5_8-jre-alpine AS builder
WORKDIR application
ARG JAR_FILE=udemy-cart-1.0.0.jar
COPY target/${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:17.0.5_8-jre-alpine
WORKDIR /opt/app
RUN addgroup --system javauser && adduser -S -s /usr/sbin/nologin -G javauser javauser
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
RUN chown -R javauser:javauser .
USER javauser
HEALTHCHECK --interval=30s --timeout=3s --retries=1 CMD wget -qO- http://localhost:9981/actuator/health/check/ | grep UP || exit 1
#ENTRYPOINT ["java", "-jar", "app.jar"]
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]