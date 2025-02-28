package rs.saga.obuka.sagashop.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.saga.obuka.sagashop.domain.User;
import rs.saga.obuka.sagashop.dto.user.AuthRequest;
import rs.saga.obuka.sagashop.dto.user.CreateUserCmd;
import rs.saga.obuka.sagashop.dto.user.UserInfo;
import rs.saga.obuka.sagashop.exception.ServiceException;
import rs.saga.obuka.sagashop.mapper.UserMapper;
import rs.saga.obuka.sagashop.security.JwtService;
import rs.saga.obuka.sagashop.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationRest {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/register")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public UserInfo register(@RequestBody @Valid CreateUserCmd cmd) throws ServiceException {
        return UserMapper.INSTANCE.userToUserInfo(userService.save(cmd));
    }

    @PostMapping("/login")
    public ResponseEntity<UserInfo> login(@RequestBody @Valid AuthRequest request) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authenticate);

            User user = UserMapper.INSTANCE.userDetailsToUser((org.springframework.security.core.userdetails.User) authenticate.getPrincipal());

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, jwtService.generateToken(user))
                    .body(UserMapper.INSTANCE.userToUserInfo(user));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
