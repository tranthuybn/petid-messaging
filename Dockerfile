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


FROM openjdk:15

COPY target/supper-app-messenging-1.0-SNAPSHOT-shaded.jar /usr/app/app.jar
COPY target/classes /usr/app/

WORKDIR /usr/app

CMD ["java", "-jar", "app.jar"]
