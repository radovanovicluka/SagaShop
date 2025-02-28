package rs.saga.obuka.sagashop.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import rs.saga.obuka.sagashop.dao.UserDAO;
import rs.saga.obuka.sagashop.domain.User;
import rs.saga.obuka.sagashop.service.UserService;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuditConfig {

    @Autowired
    private UserService userService;

    @Bean
    public AuditorAware<User> auditorAware() {
        return new AuditorAwareImpl(userService);
    }

}