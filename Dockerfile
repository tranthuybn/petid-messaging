#FROM tomcat:latest
#RUN rm -rf /usr/local/tomcat/webapps/
#
#RUN mv /usr/local/tomcat/webapps.dist /usr/local/tomcat/webapps
#ENV CATALINA_OPTS="-Xmx512m -Xms256m -Xss1m"
#
#COPY /target/supper-app-messenging-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
#
##RUN sed -i 's/port="8080"/port="8689"/g' /usr/local/tomcat/conf/server.xml
#EXPOSE 8080
#
#CMD ["catalina.sh", "run"]

FROM maven:3.6.3-jdk-11 AS build

COPY src /home/app/src
COPY pom.xml /home/app

RUN mvn -f /home/app/pom.xml clean install

FROM openjdk:15

COPY --from=build /home/app/target/supper-app-messenging-1.0-SNAPSHOT-shaded.jar /usr/app/app.jar
COPY src/main/resources /usr/app/

WORKDIR /usr/app

CMD ["java", "-jar", "app.jar"]
