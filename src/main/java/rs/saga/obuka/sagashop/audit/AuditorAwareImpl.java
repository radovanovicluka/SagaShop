package rs.saga.obuka.sagashop.audit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import rs.saga.obuka.sagashop.service.UserService;

import java.util.Optional;

@Slf4j
//@Component
@RequiredArgsConstructor
class AuditorAwareImpl implements AuditorAware<rs.saga.obuka.sagashop.domain.User> {

//    @Autowired
    private final UserService userService;

    @Override
    public Optional<rs.saga.obuka.sagashop.domain.User> getCurrentAuditor() {
        log.info("UNUTAR AUDITOR AWARE");
        User user = (User)(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        return Optional.of(userService.findByUsername(user.getUsername()));
    }
}