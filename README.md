# Vert.x Example Maven Project

Example project for creating a Vert.x module with a Gradle build.

By default this module contains a simple Java verticle which listens on the event bus and responds to `ping!`
messages with `pong!`.

This example also shows you how to write tests in Java, Groovy, Ruby and Python

mvn archetype：
1、clone https://github.com/vert-x/vertx-maven
2、git checkout 2.0.8-final
2、进入archetype目录，修改pom.xml，去掉gpg
3、mvn clean install
4、mvn archetype:generate，选择local：vertx-maven-archetype

run vertx:

vertx run src/main/resources/JavaSourcePingVerticle.java

vertx run src/main/java/com/m3958/vertx/backuper/PingVerticle.java

vertx runzip target/backuper-1.0-SNAPSHOT-mod.zip

vertx runmod com.m3958.vertx~backuper~1.0-SNAPSHOT

vertx runzip my-mod~2.0.1.zip