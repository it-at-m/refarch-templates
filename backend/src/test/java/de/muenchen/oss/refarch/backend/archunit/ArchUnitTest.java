package de.muenchen.oss.refarch.backend.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import de.muenchen.oss.refarch.backend.MicroServiceApplication;
import de.muenchen.oss.refarch.backend.archunit.rules.Rules;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ArchUnitTest {

    private static JavaClasses allTestClasses;

    @BeforeAll
    static void init() {
        allTestClasses = new ClassFileImporter()
                .withImportOption(new ImportOption.OnlyIncludeTests())
                .importPackages(MicroServiceApplication.class.getPackage().getName());
    }

    public static Stream<Arguments> allTestClassesRulesToVerify() {
        return Stream.of(
                Arguments.of("Test classes must end with 'Test'",
                        Rules.RULE_TESTCLASSES_END_WITH_TEST_CONVENTION_MATCHED),
                Arguments.of("Test methods must follow 'given_then' naming convention",
                        Rules.RULE_TEST_NAMING_CONVENTION_GIVEN_THEN_MATCHED),
                Arguments.of("BeforeEach methods must follow naming convention",
                        Rules.RULE_BEFORE_EACH_NAMING_CONVENTION_MATCHED),
                Arguments.of("AfterEach methods must follow naming convention",
                        Rules.RULE_AFTER_EACH_NAMING_CONVENTION_MATCHED),
                Arguments.of("Test methods should be package-private",
                        Rules.RULE_TEST_METHODS_ARE_PACKAGE_PRIVATE_CONVENTION_MATCHED));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("allTestClassesRulesToVerify")
    void givenAllArchUnitRulesForAllTestClasses_thenRunArchUnitTests(final ArgumentsAccessor arguments) {
        arguments.get(1, ArchRule.class).check(allTestClasses);
    }

}
