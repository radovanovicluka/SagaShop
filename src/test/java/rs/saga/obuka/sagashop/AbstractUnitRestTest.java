package rs.saga.obuka.sagashop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import rs.saga.obuka.sagashop.dao.UserDAO;
import rs.saga.obuka.sagashop.security.JwtService;

/**
 * @author: Ana Dedović
 * Date: 30.06.2021.
 **/
@ExtendWith(SpringExtension.class)
@ContextConfiguration
public abstract class AbstractUnitRestTest {

    protected MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private UserDAO userDAO;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    protected ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Configuration
    @EnableAutoConfiguration
    @ComponentScan(basePackages = "rs.saga.obuka.sagashop",
            excludeFilters = {
                    @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
                    @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class)})
    static class TestConfig {
    }

}
