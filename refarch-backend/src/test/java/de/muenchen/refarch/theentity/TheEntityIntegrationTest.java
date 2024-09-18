package de.muenchen.refarch.theentity;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.refarch.TestConstants;
import de.muenchen.refarch.theentity.dto.TheEntityRequestDTO;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static de.muenchen.refarch.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static de.muenchen.refarch.TestConstants.SPRING_TEST_PROFILE;
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

    @BeforeEach
    public void setUp() {

        // Create and save an example entity
        TheEntity entity = new TheEntity();
        entity.setTextAttribute("Test");
        testEntityId = theEntityRepository.save(entity).getId();

    }

    @Test
    void testGetTheEntityById() throws Exception {
        mockMvc.perform(get("/theEntity/{theEntityID}", testEntityId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testEntityId.toString())));
    }

    @Test
    void testGetTheEntitiesByPageAndSize() throws Exception {
        mockMvc.perform(get("/theEntity")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    void testSaveTheEntity() throws Exception {
        TheEntityRequestDTO requestDTO = new TheEntityRequestDTO("Test");
        String requestBody = objectMapper.writeValueAsString(requestDTO);

        mockMvc.perform(post("/theEntity")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.textAttribute", is("Test")));
    }

    @Test
    void testUpdateTheEntity() throws Exception {
        TheEntityRequestDTO requestDTO = new TheEntityRequestDTO("Test");
        String requestBody = objectMapper.writeValueAsString(requestDTO);

        mockMvc.perform(put("/theEntity/{theEntityId}", testEntityId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testEntityId.toString())))
                .andExpect(jsonPath("$.textAttribute", is("Test")));
    }

    @Test
    void testDeleteTheEntity() throws Exception {
        mockMvc.perform(delete("/theEntity/{theEntityId}", testEntityId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}
