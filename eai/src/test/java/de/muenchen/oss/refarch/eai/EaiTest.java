package de.muenchen.oss.refarch.eai;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit6.CamelSpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/// Demonstrates how the complete EAI can be tested from startup to shutdown.
///
/// Uses the test configuration defined in `test/application-test.yml`.
@SpringBootTest
@CamelSpringBootTest
@ActiveProfiles(TestConstants.SPRING_TEST_PROFILE)
class EaiTest {

    @Produce(EaiRouteBuilder.DIRECT_ROUTE)
    private ProducerTemplate producer;

    @EndpointInject("mock:example")
    private MockEndpoint output;

    /// The test sends a message through the EAI and verifies that it reaches the expected mock endpoint.
    @Test
    void givenMessage_thenSendToMockShouldSucceed() throws InterruptedException {
        final String message = "Hello Test !";
        output.expectedMessageCount(1);

        producer.sendBody(message);

        output.assertIsSatisfied();
        assertEquals(message, output.getExchanges().getFirst().getMessage().getBody(String.class));
    }

}
