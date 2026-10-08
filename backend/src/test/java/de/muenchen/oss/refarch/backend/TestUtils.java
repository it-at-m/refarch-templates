package de.muenchen.oss.refarch.backend;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.yaml.snakeyaml.Yaml;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings({ "PMD.TestClassWithoutTestCases" })
public final class TestUtils {

    /// Utility function to extract an image reference from the `stack/docker-compose.yaml` file. This
    /// is useful for container definitions with Testcontainers and keeps versions used in integration
    /// testing in sync with versions used for development.
    ///
    /// **Note:** The tag is being removed due to Testcontainers not supporting supplying both tag and
    /// SHA value.
    ///
    /// @param serviceName docker service name
    /// @return image reference with name and SHA
    public static String getImageFromDockerCompose(final String serviceName) {
        final Yaml yaml = new Yaml();

        try (InputStream input = Files.newInputStream(
                Path.of("../stack/docker-compose.yml"))) {
            final Map<String, Object> compose = yaml.load(input);

            final Object image = ((Map<?, ?>) ((Map<?, ?>) compose.get("services"))
                    .get(serviceName))
                    .get("image");

            if (!(image instanceof String)) {
                throw new IllegalStateException(
                        String.format(
                                "Could not find services.%s.image in docker-compose.yml",
                                serviceName));
            }

            return removeImageTag((String) image);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Could not read docker-compose.yml",
                    e);
        }
    }

    private static String removeImageTag(final String imageName) {
        final int digestSeparator = imageName.indexOf("@");

        if (digestSeparator >= 0) {
            final String repository = imageName.substring(0, digestSeparator);
            final String digest = imageName.substring(digestSeparator);

            // Remove the tag from the repository, if present.
            final int tagSeparator = repository.lastIndexOf(":");
            final int pathSeparator = repository.lastIndexOf("/");
            final String imageWithoutTag = tagSeparator > pathSeparator
                    ? repository.substring(0, tagSeparator)
                    : repository;

            return imageWithoutTag + digest;
        }

        return imageName;
    }

}
