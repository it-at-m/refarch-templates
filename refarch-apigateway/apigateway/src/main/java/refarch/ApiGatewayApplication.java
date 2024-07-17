/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package refarch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * To do some base configuration for the non blocking client-server framework
 * named Netty via properties use the properties listed in the link down below:
 *
 * @see <a href=
 *      "https://projectreactor.io/docs/netty/release/api/constant-values.html">https://projectreactor.io/docs/netty/release/api/constant-values.html</a>
 *
 *      As listed below, this above mentioned properties should be set before the application
 *      startup:
 *
 *      <ul>
 *      <li>As command line argument: e.g. -Dreactor.netty.pool.maxConnections=1000.
 *      <li>As environmental property in Openshift: e.g. with key REACTOR_NETTY_POOL_MAXCONNECTIONS
 *      and value 1000.
 *      <li>As programatically set property before call {@link SpringApplication#run} in
 *      {@link ApiGatewayApplication#main}: e.g.
 *      <code>System.setProperty("reactor.netty.pool.maxConnections", "1000");</code>.
 *      </ul>
 *
 *      To get more information about Spring Cloud Gateway visit the following link:
 *
 * @see <a href=
 *      "https://cloud.spring.io/spring-cloud-gateway/reference/html/">https://cloud.spring.io/spring-cloud-gateway/reference/html/</a>
 */
@SpringBootApplication(scanBasePackages = { "refarch" })
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

}
