FROM cr.siemens.com/container-hardening-service/releases/openjre17:latest
ARG APP_USER
ARG APP_DIR
WORKDIR $APP_DIR
#RUN apk --no-cache add busybox-extras
ADD docker/harden.sh / 
#RUN wget https://truststore.pki.rds.amazonaws.com/eu-central-1/eu-central-1-bundle.pem --directory-prefix=$APP_DIR 
#RUN keytool -import -file $APP_DIR/eu-central-1-bundle.pem -alias docdb -storepass changeit -keystore rds-truststore.jks -noprompt
#RUN wget https://truststore.pki.rds.amazonaws.com/global/global-bundle.pem --directory-prefix=$APP_DIR
#RUN keytool -import -alias docdb -cacerts -file $APP_DIR/eu-central-1-bundle.pem -noprompt -storepass changeit
RUN wget https://truststore.pki.rds.amazonaws.com/eu-central-1/eu-central-1-bundle.pem
RUN cat eu-central-1-bundle.pem|awk 'split_after==1{n++;split_after=0}  /-----END CERTIFICATE-----/ {split_after=1}  {if(length($0) > 0) print > "cert" n ".pem"}'
RUN keytool -import -alias docdb -cacerts -file $APP_DIR/cert3.pem -noprompt -storepass changeit   
#COPY ./deploy/rds-truststore.jks /app/
COPY ./deploy/springboot.jks /app/
RUN chmod 700 /harden.sh \
	&& sh -c "/harden.sh" \
	&& rm -rf /harden.sh

VOLUME /tmp
EXPOSE 8080
#EXPOSE 8444
ADD target/*.jar app.jar
ENV JAVA_OPTS="-Xms512m -Xmx1024m"
#ENV JAVA_OPTS ="-Xms256M -Xmx1024M"
USER $APP_USER
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar app.jar" ]
