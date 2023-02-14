# Java jsmpp implementation

This is a spring boot, java Jsmpp integration. The application uses java org.jsmpp dependancy for sending and receiving sms using smpp bind. The application can be run as; Transceiver(TRX) -> sending & reciving, Transimter(TX) -> sending only, or Receiver(RX) -> receiving only. configurations can be set in the application.yaml or by overidding defaults configs at run time through -D....(property name) java options.

Applications runs on java 17 and spring boot version 3.0.2.

Logging is done through logback, configuration file -> logback-spring.xml

## Available scripts

In the project directory, you can run:

### `mvn clean install`

This command clean builds the project.

### `mvn spring-boot:run`

This command runs the build application. If the application is run as a TRX or TX, it exposes a rest endpoint (http://127.0.0.1:8080/smpp/sms) which can be used to send an sms.

### `java -jar target/crud-0.0.1.jar`

This command is used to run the generated fat jar and its the same jar that can be copied & deployed in production environments.
