# ---------- Build stage ----------
FROM gradle:8.9.0-jdk21 AS builder
WORKDIR /workspace

# 캐시 최적화: wrapper/의존성 먼저 복사
COPY gradle gradle
COPY gradlew ./
COPY settings.gradle ./
COPY build.gradle ./

# 의존성만 먼저 받아서 캐시
RUN ./gradlew dependencies --no-daemon || true

# 소스 복사 후 빌드
COPY src src
RUN ./gradlew clean bootJar -x test --no-daemon

# ---------- Run stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# (선택) 타임존 설정
ENV TZ=Asia/Seoul

# 빌드 산출물 복사
COPY --from=builder /workspace/build/libs/*.jar app.jar

# 컨테이너 외부에서 바꿀 수 있는 JMV 옵션 훅
ENV JAVA_OPTS=""

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]