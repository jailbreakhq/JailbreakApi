web: java $JAVA_OPTS -jar auth-service/target/auth-service-0.0.1-SNAPSHOT.jar db migrate auth-service/jailbreak.yml && java $JAVA_OPTS -Ddw.server.connector.port=$PORT -jar auth-service/target/auth-service-0.0.1-SNAPSHOT.jar server auth-service/jailbreak.yml