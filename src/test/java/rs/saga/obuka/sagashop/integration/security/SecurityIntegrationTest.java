package rs.saga.obuka.sagashop.integration.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.context.WebApplicationContext;
import rs.saga.obuka.sagashop.AbstractIntegrationTest;
import rs.saga.obuka.sagashop.domain.Category;
import rs.saga.obuka.sagashop.dto.category.CreateCategoryCmd;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SecurityIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private int port = 8082;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtTokenNovica;
    private String jwtTokenLuka;

    private String url = "http://localhost:" + port;

    @BeforeEach
    void loginUser() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // Ensure correct Content-Type

        String requestBody = "{\"username\": \"novicat\", \"password\": \"pass2\"}";
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url + "/auth/login", request, String.class);

        jwtTokenNovica = responseEntity.getHeaders().get("Authorization").get(0);
        assertThat(jwtTokenNovica).isNotEmpty();

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // Ensure correct Content-Type

        requestBody = "{\"username\": \"lukar\", \"password\": \"pass1\"}";
        request = new HttpEntity<>(requestBody, headers);

        responseEntity = restTemplate.postForEntity(url + "/auth/login", request, String.class);

        jwtTokenLuka = responseEntity.getHeaders().get("Authorization").get(0);
        assertThat(jwtTokenLuka).isNotEmpty();
    }

    @Test
    public void testSaveCategoryLogged() throws JsonProcessingException {
        CreateCategoryCmd cmd = new CreateCategoryCmd("Category1", "Description1", null, null);
        String jsonInString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cmd);

        String path = url + "/category";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwtTokenNovica);
        httpHeaders.add("Content-Type", "application/json");

        HttpEntity<String> httpEntity = new HttpEntity<>(jsonInString, httpHeaders);

        ResponseEntity<Category> response = restTemplate.exchange(path, HttpMethod.POST, httpEntity, Category.class);
        assertEquals(200, response.getStatusCodeValue());

    }

    @Test
    public void testSaveCategoryWrongAuthority() throws JsonProcessingException {
        CreateCategoryCmd cmd = new CreateCategoryCmd("Category1", "Description1", null, null);
        String jsonInString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cmd);

        String path = url + "/category";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwtTokenLuka);
        httpHeaders.add("Content-Type", "application/json");

        HttpEntity<String> httpEntity = new HttpEntity<>(jsonInString, httpHeaders);

        ResponseEntity<Category> response = restTemplate.exchange(path, HttpMethod.POST, httpEntity, Category.class);
        assertEquals(403, response.getStatusCodeValue());

    }

    @Disabled
    @Test
    public void testSaveCategoryNotLogged() throws JsonProcessingException {
        CreateCategoryCmd cmd = new CreateCategoryCmd("Category1", "Description1", null, null);
        String jsonInString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cmd);

        String path = url + "/category";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");

        HttpEntity<String> httpEntity = new HttpEntity<>(jsonInString, httpHeaders);

        ResponseEntity<Category> response = restTemplate.exchange(path, HttpMethod.POST, httpEntity, Category.class);
        assertEquals(401, response.getStatusCodeValue());

    }

}
