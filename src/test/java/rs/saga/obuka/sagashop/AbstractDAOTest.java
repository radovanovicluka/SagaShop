package rs.saga.obuka.sagashop;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
}
