package de.muenchen.refarch.theentity;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.refarch.TestConstants;
import de.muenchen.refarch.theentity.dto.TheEntityRequestDTO;
import de.muenchen.refarch.theentity.dto.TheEntityResponseDTO;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static de.muenchen.refarch.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static de.muenchen.refarch.TestConstants.SPRING_TEST_PROFILE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles(profiles = { SPRING_TEST_PROFILE, SPRING_NO_SECURITY_PROFILE })
class TheEntityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    @ServiceConnection
    @SuppressWarnings("unused")
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(
            DockerImageName.parse(TestConstants.TESTCONTAINERS_POSTGRES_IMAGE));

    private UUID testEntityId;

    @Autowired
    private TheEntityRepository theEntityRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    public void setUp() {
        final TheEntity exampleEntity = new TheEntity();
        exampleEntity.setTextAttribute("Test");
        testEntityId = theEntityRepository.save(exampleEntity).getId();
    }

    @AfterEach
    public void tearDown() {
        theEntityRepository.deleteById(testEntityId);
    }

    @Test
    void givenEntityId_whenGetEntityById_thenReturnEntity() throws Exception {
        mockMvc.perform(get("/theEntity/{theEntityID}", testEntityId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testEntityId.toString())));
    }

    @Test
    void givenEntityId_whenGetEntityByIdWithTestRestTemplate_thenReturnEntity() {
        final TheEntityResponseDTO expectedDTO = new TheEntityResponseDTO(testEntityId, "Test");
        final String url = "/theEntity/{theEntityID}";
        final ResponseEntity<TheEntityResponseDTO> response = testRestTemplate.getForEntity(url, TheEntityResponseDTO.class, testEntityId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);

        assertThat(response.getBody()).usingRecursiveComparison().isEqualTo(expectedDTO);
    }

    @Test
    void givenPageNumberAndPageSize_whenGetEntitiesByPageAndSize_thenReturnPageOfEntities() throws Exception {
        mockMvc.perform(get("/theEntity")
                .param("pageNumber", "0")
                .param("pageSize", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    void givenEntity_whenSaveEntity_thenEntityIsSaved() throws Exception {
        final TheEntityRequestDTO requestDTO = new TheEntityRequestDTO("Test1");
        final String requestBody = objectMapper.writeValueAsString(requestDTO);

        mockMvc.perform(post("/theEntity")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.textAttribute", is(requestDTO.textAttribute())));
    }

    @Test
    void givenEntity_whenUpdateEntity_thenEntityIsUpdated() throws Exception {
        final TheEntityRequestDTO requestDTO = new TheEntityRequestDTO("Test2");
        final String requestBody = objectMapper.writeValueAsString(requestDTO);

        mockMvc.perform(put("/theEntity/{theEntityId}", testEntityId)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testEntityId.toString())))
                .andExpect(jsonPath("$.textAttribute", is(requestDTO.textAttribute())));
    }

    @Test
    void givenEntityId_whenDeleteEntity_thenEntityIsDeleted() throws Exception {
        mockMvc.perform(delete("/theEntity/{theEntityId}", testEntityId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
