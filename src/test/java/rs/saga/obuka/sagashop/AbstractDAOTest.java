package rs.saga.obuka.sagashop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rs.saga.obuka.sagashop.dao.UserDAO;
import rs.saga.obuka.sagashop.domain.User;
import rs.saga.obuka.sagashop.dto.user.UserInfo;
import rs.saga.obuka.sagashop.exception.DAOException;
import rs.saga.obuka.sagashop.service.UserService;

/**
 * @author: Ana Dedović
 * Date: 13.07.2021.
 **/
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SagashopApplicationTest.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts="classpath:db/test-data-before.sql", executionPhase=Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts="classpath:db/test-data-after.sql", executionPhase= Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SuppressWarnings("unused")
public abstract class AbstractDAOTest {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        UserInfo user = userService.findById(1L);

        System.out.println(user.getUsername());

//        try {
//            userDAO.save(user);
//        } catch (DAOException e) {
//            throw new RuntimeException(e);
//        }

        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), "default"));

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        UserInfo userInfo = userService.findById(1L);
        System.out.println(userInfo.getUsername());
    }

}
