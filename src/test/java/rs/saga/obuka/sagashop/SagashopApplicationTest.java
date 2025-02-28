package rs.saga.obuka.sagashop;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = "rs.saga.obuka.sagashop")
@PropertySource(value = {"classpath:application.properties"})
public class SagashopApplicationTest {

}

