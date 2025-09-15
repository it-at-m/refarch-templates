package de.muenchen.refarch.archunit.rules;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static de.muenchen.refarch.archunit.rules.TestClassesEndWithTestCondition.haveTopEnclosingClassEndingWithTest;

import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.elements.MethodsShouldConjunction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MethodRules {

    public static final MethodsShouldConjunction RULE_TEST_NAMING_CONVENTION_SHOULD_WHEN_MATCHED = methods()
            .that().areAnnotatedWith(Test.class).or().areAnnotatedWith(ParameterizedTest.class)
            .should().haveNameMatching("^given[A-Z][a-zA-Z]+_then[A-Z][a-zA-Z]+$");

    public static final ArchRule RULE_BEFORE_EACH_NAMING_CONVENTION_MATCHED = methods()
            .that().areAnnotatedWith(BeforeEach.class).should().haveNameMatching("setUp")
            .allowEmptyShould(true);

    public static final ArchRule RULE_AFTER_EACH_NAMING_CONVENTION_MATCHED = methods()
            .that().areAnnotatedWith(AfterEach.class).should().haveNameMatching("tearDown")
            .allowEmptyShould(true);

    public static final ArchRule RULE_TEST_METHODS_ARE_PACKAGE_PRIVATE_CONVENTION_MATCHED = methods()
            .that().areAnnotatedWith(Test.class).or().areAnnotatedWith(ParameterizedTest.class).should()
            .notHaveModifier(JavaModifier.PROTECTED)
            .andShould().notHaveModifier(JavaModifier.PRIVATE)
            .andShould().notHaveModifier(JavaModifier.PUBLIC);

    public static final ArchRule RULE_TESTCLASSES_END_WITH_TEST_CONVENTION_MATCHED = methods()
            .that().areAnnotatedWith(Test.class).or().areAnnotatedWith(ParameterizedTest.class)
            .should(haveTopEnclosingClassEndingWithTest);
}
