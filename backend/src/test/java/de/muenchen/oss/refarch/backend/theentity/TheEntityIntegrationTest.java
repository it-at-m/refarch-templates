package de.muenchen.oss.refarch.backend.theentity;

import static de.muenchen.oss.refarch.backend.TestConstants.SPRING_TEST_PROFILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import de.muenchen.oss.refarch.backend.TestConstants;
import de.muenchen.oss.refarch.backend.TestSecurityConfiguration;
import de.muenchen.oss.refarch.backend.theentity.dto.TheEntityRequestDTO;
import de.muenchen.oss.refarch.backend.theentity.dto.TheEntityResponseDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@ActiveProfiles(profiles = { SPRING_TEST_PROFILE })
@Import(TestSecurityConfiguration.class)
class TheEntityIntegrationTest {

    @Autowired
    private RestTestClient restTestClient;

    @Container
    @ServiceConnection
    @SuppressWarnings("unused")
    private static final PostgreSQLContainer POSTGRE_SQL_CONTAINER = new PostgreSQLContainer(
            DockerImageName.parse(TestConstants.TESTCONTAINERS_POSTGRES_IMAGE));

    private UUID testEntityId;

    @Autowired
    private TheEntityRepository theEntityRepository;

    @BeforeEach
    public void setUp() {
        theEntityRepository.deleteAll();
        final TheEntity exampleEntity = new TheEntity();
        exampleEntity.setTextAttribute("Test");
        testEntityId = theEntityRepository.save(exampleEntity).getId();
    }

    @AfterEach
    public void tearDown() {
        theEntityRepository.deleteById(testEntityId);
    }

    @Nested
    class GetEntity {
        @Test
        void givenEntityId_thenReturnEntity() {
            restTestClient
                    .get()
                    .uri("/theEntity/{theEntityID}", testEntityId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer reader")
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBody(TheEntityResponseDTO.class)
                    .value(theEntityResponseDTO -> {
                        assertNotNull(theEntityResponseDTO);
                        assertThat(theEntityResponseDTO.id()).isEqualTo(testEntityId);
                    });
        }
    }

    @Nested
    class GetEntitiesPage {
        @Test
        void givenPageNumberAndPageSize_thenReturnPageOfEntities() {
            restTestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/theEntity")
                            .queryParam("pageNumber", "0")
                            .queryParam("pageSize", "10")
                            .build())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer reader")
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("$.content")
                    .value(new ParameterizedTypeReference<List<TheEntityResponseDTO>>() {
                    }, content -> assertThat(content.size()).isEqualTo(1));
        }
    }

    @Nested
    class SaveEntity {
        @Test
        void givenEntity_thenEntityIsSaved() {
            final String value = "Test1";
            final TheEntityRequestDTO requestDTO = new TheEntityRequestDTO(value);

            final TheEntityResponseDTO responseDTO = restTestClient.post()
                    .uri("/theEntity")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer writer")
                    .body(requestDTO)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBody(TheEntityResponseDTO.class)
                    .value(theEntityResponseDTO -> {
                        assertNotNull(theEntityResponseDTO);
                        assertThat(theEntityResponseDTO.textAttribute()).isEqualTo(requestDTO.textAttribute());
                    })
                    .returnResult()
                    .getResponseBody();

            assertThat(responseDTO).isNotNull();
            final Optional<TheEntity> theEntity = theEntityRepository.findById(responseDTO.id());
            assertThat(theEntity).isPresent();
            assertThat(theEntity.get().getTextAttribute()).isEqualTo(value);
        }
    }

    @Nested
    class UpdateEntity {
        @Test
        void givenEntity_thenEntityIsUpdated() {
            final String newValue = "Test2";
            final TheEntityRequestDTO requestDTO = new TheEntityRequestDTO(newValue);

            restTestClient.put()
                    .uri("/theEntity/{theEntityId}", testEntityId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer writer")
                    .body(requestDTO)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBody(TheEntityResponseDTO.class)
                    .value(theEntityResponseDTO -> {
                        assertNotNull(theEntityResponseDTO);
                        assertThat(theEntityResponseDTO.id()).isEqualTo(testEntityId);
                        assertThat(theEntityResponseDTO.textAttribute()).isEqualTo(requestDTO.textAttribute());
                    });

            assertThat(theEntityRepository.findById(testEntityId).orElseThrow().getTextAttribute()).isEqualTo(newValue);
        }
    }

    @Nested
    class DeleteEntity {
        @Test
        void givenEntityId_thenEntityIsDeleted() {
            restTestClient.delete()
                    .uri("/theEntity/{theEntityID}", testEntityId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer writer")
                    .exchange()
                    .expectStatus().isOk();

            assertThat(theEntityRepository.findById(testEntityId)).isEmpty();
        }
    }

}
