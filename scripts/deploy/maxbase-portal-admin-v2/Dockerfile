FROM livingobjects/jre8
ENV JAVA_OPTS=""
VOLUME /tmp
ADD maxbase-portal-admin-v2-1.0.0.jar /app.jar
RUN sh -c 'touch /app.jar'
CMD exec java -Xms512m -Xmx512m $JAVA_OPTS -jar /app.jar
