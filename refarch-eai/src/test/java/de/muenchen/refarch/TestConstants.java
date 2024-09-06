package de.muenchen.refarch;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@SuppressWarnings("PMD.TestClassWithoutTestCases")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestConstants {

    public static final String SPRING_TEST_PROFILE = "test";

    public static final String SPRING_JSON_LOGGING_PROFILE = "json-logging";

}
