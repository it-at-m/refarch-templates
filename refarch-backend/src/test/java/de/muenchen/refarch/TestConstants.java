package de.muenchen.refarch;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("PMD.TestClassWithoutTestCases")
public final class TestConstants {

    public static final String SPRING_TEST_PROFILE = "test";

    public static final String SPRING_NO_SECURITY_PROFILE = "no-security";

    @NoArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class TheEntityDto extends RepresentationModel {

        private String textAttribute;

    }

}
