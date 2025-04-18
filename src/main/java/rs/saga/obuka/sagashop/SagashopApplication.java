package rs.saga.obuka.sagashop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@PropertySource(value = "classpath:application.properties")
public class SagashopApplication {

    public static void main(String[] args) {
        SpringApplication.run(SagashopApplication.class, args);
    }

}
