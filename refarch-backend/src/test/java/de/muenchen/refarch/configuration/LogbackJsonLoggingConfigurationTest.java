package de.muenchen.refarch.configuration;

import static de.muenchen.refarch.TestConstants.SPRING_JSON_LOGGING_PROFILE;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.MDC;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles(SPRING_JSON_LOGGING_PROFILE)
@ExtendWith(OutputCaptureExtension.class)
@Slf4j
@DirtiesContext // force logback config reset after test
class LogbackJsonLoggingConfigurationTest {

    @Configuration
    static class TestConfiguration {
    }

    private static final String EXCEPTION_MESSAGE = "EXC_MESSAGE";
    private static final Pattern STACKTRACE_PATTERN = Pattern.compile("stack_trace\":\"([^\"]*)\"");

    private Throwable exception;

    @BeforeEach
    void setup() {
        // prepare an exception with huge stacktrace contents
        exception = genExceptionStack(new IllegalArgumentException("rootcause"), 40);
        // make sure generated stacktrace is long enough to test shortening
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String untouchedStacktrace = sw.toString();
        assertThat(untouchedStacktrace.length()).isGreaterThan(16000);
    }

    @AfterEach
    void tearDown() {
        // make sure MDC is clean after tests
        MDC.clear();
    }

    @Test
    void puts_mdc_in_json(CapturedOutput output) {
        MDC.put("traceId", "myTraceId");
        MDC.put("spanId", "mySpanId");
        String message = "my message";
        log.info(message);

        String line = findLogmessageInOutput(output, message);

        assertThat(line).contains("myTraceId").contains("mySpanId");
    }

    @Test
    void json_prints_root_cause_of_stacktrace_first(CapturedOutput output) {
        MDC.put("traceId", "myTraceId");
        MDC.put("spanId", "mySpanId");
        log.error(EXCEPTION_MESSAGE, exception);

        String line = findLogmessageInOutput(output, EXCEPTION_MESSAGE);

        assertThat(line.indexOf("rootcause")).isLessThan(line.indexOf("stackmessage-#1"));
    }

    @Test
    void shortens_length_of_stacktrace(CapturedOutput output) {
        log.error(EXCEPTION_MESSAGE, exception);

        String line = findLogmessageInOutput(output, EXCEPTION_MESSAGE);

        Matcher matcher = STACKTRACE_PATTERN.matcher(line);
        if (matcher.find()) {
            String group = matcher.group(1);
            // apparently ShortenedThrowableConverter is not a 100% accurate with "maxLength" - but that is fine
            assertThat(group.length()).isCloseTo(8192, Percentage.withPercentage(10.0d));
        } else {
            Assertions.fail("Expected to find a stack_trace element in JSON structure, but was not present.");
        }
    }

    private String findLogmessageInOutput(CapturedOutput output, String expected) {
        String fullOutput = output.getOut();
        String line = Arrays.stream(fullOutput.split("\n"))
                .filter(s -> s.contains(expected))
                .findFirst().orElseThrow();
        return line;
    }

    private static Throwable genExceptionStack(Throwable root, int index) {
        if (index > 0) {
            return new IllegalArgumentException("stackmessage-#%d: (%s)".formatted(index, StringUtils.repeat("abcd ", 20)), genExceptionStack(root, --index));
        } else {
            return root;
        }
    }
}
