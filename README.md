
# The-Liar-game
> SpringBoot 라이어 게임 (MSA 리펙토링) 개인 프로젝트입니다.

![](../header.png)


## 구조도
![image](https://user-images.githubusercontent.com/88478829/215472607-31bb8f28-7151-4e7b-aee3-375b464b506b.png)

![](../header.png)


## 개발 환경 설정
```
운영체제: ubuntu20.04
Spring: springboot 3.0.2
Java: openjdk 17.0.5
Mysql: 8.0.32
Redis: 6.0.16
Rabbitmq: 3.11.7
Erlang: 25.2.1
Gradle: 7.6
IDE: Intellij
```
![](../header.png)

## 업데이트 내역

* 0.0.1 기본 infra 구축 
* 0.0.2 gateway-server, member-service 인증/인가 처리 구현
* 0.0.3 wait-service 도메인 개발 완료, docs 발행
* 0.0.4 game-service 도메인 개발 중 입니다. (webflux)

![](../header.png)
## 정보
|구성|이름|포트|비고
|:---:|:---:|:---:|:---:|
|Infra|eureka-server|8761|SpringCloud|
|Infra|gateway-server|8000|SpringCloud|
|Infra|config-server|8888|https://github.com/gosekose/The-Liar-game-config|
|Infra|rabitMq-server|5672|SpringCloud|
|Business|member-server|8080|SpringRestful|
|Business|wait-server|8081|SpringRestful|
|Business|game-server|8082|SpringWebflux|
|DB|authentication-server|6379|gateway, member-server 토큰 인증 서버|
|DB|wait-redis-server|6380|wait-server 전용|
|DB|game-redis-server|6381|game-server 전용|
|DB|mysql-server|3306|공통 데이터베이스|

![](../header.png)


## 단위/기능 테스트 실행 (ubuntu)
단위/기능 테스트를 진행할 때는 rdbms(dev: h2) 와 redis, rabbitmq, config.server를 반드시 실행시켜주어야 합니다. 

member-service 실행시 
```
./h2.sh
java -jar config-server.jar
java -jar eureka-server.jar
java -jar gateway-server.jar

systemctl start rabbitmq-server
redis-server --port 6379
redis-server --port 6380
redis-cli -h 127.0.0.1 -p 6380
```

wait-service 실행시 
```
./h2.sh
java -jar config-server.jar
systemctl start rabbitmq-server
redis-server --port 6379
redis-server --port 6380
redis-cli -h 127.0.0.1 -p 6380
```

## 활동 주요 로그 (커밋 로그) 
1. eureka, gateway를 추가하고 도메인 서버와 연결하였습니다. [commit ac614b5](https://github.com/gosekose/The-Liar-game/pull/15/commits/ac614b5bf38ad77511e2da8e798c469a4b5c2393)
2. SpringRestDocs를 활용하여 API 명세를 구성하였습니다. [commit 2bc4096](https://github.com/gosekose/The-Liar-game/commit/2bc409635debee55b638cd233381b7965eaa7aff)
3. 비즈니스 서비스에 공통으로 존재한 JWT 검증을 Gateway Authentication(jwt) 필터로 처리하였습니다. [commit 9ea97d6](https://github.com/gosekose/The-Liar-game/commit/9ea97d65f0e98c86874d66356ec42adb45186f13)
4. Config-server를 추가하여 yml 공통 정보를 깃에서 관리하고 암호화 하였습니다. [commit 4a95537b](https://github.com/gosekose/The-Liar-game/commit/4a95537b2d269227e0ebe9ea3964ec8946202694)
5. wait-server 전용 redis를 구성하였습니다. [commit 8d87036](https://github.com/gosekose/The-Liar-game/commit/8d8703624b712c9c0b24dd8ebf678337f3af6cd0) 
6. waitRoom 검색 조건에 따른 redis 검색 전략을 구성하기 위한 Connection 인터페이스 설계를 구현하였습니다. [commit 6f5a6ea](https://github.com/gosekose/The-Liar-game/commit/6f5a6eaf9918e9de71b1542973242fe65c089675)
7. waitRoom의 joinPolicy 정책을 인터페이스화하여 WaitRoomService에 의존성 주입하였습니다. [commit 81a063f](https://github.com/gosekose/The-Liar-game/commit/81a063fb2a65a62be0922dc0395c2ed602662fa0) 
8. game 시작 전에 요청되는 wait-service의 prePross와 game 종료 후에 요청되는 wait-service의 postProcess를 모든 로직 테스트 구문을 작성하였습니다. [commit c06f2d4](https://github.com/gosekose/The-Liar-game/commit/c06f2d4486bdd3435e8a336a42efb7d01f0ca734)
