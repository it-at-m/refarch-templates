package de.muenchen.refarch;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * The test is used to demonstrate how any test configuration
 * (test/application-test.yml) to test the
 * entire EAI can be tested from start to shut down with one test call.
 **/
@SpringBootTest
@CamelSpringBootTest
@ActiveProfiles(TestConstants.SPRING_TEST_PROFILE)
class EaiTest {

    @SuppressWarnings("unused") // field is auto-injected by camel
    @Produce(EaiRouteBuilder.DIRECT_ROUTE)
    private ProducerTemplate producer;

    @SuppressWarnings("unused") // field is auto-injected by camel
    @EndpointInject("mock:output")
    private MockEndpoint output;

    @Test
    void sendToMockTest() throws InterruptedException {
        final String message = "Hello Test !";
        output.expectedMessageCount(1);

        producer.sendBody(message);

        output.assertIsSatisfied();
        assertEquals(message, output.getExchanges().get(0).getMessage().getBody(String.class));
    }

}
