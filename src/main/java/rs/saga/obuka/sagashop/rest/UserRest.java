package rs.saga.obuka.sagashop.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.saga.obuka.sagashop.domain.User;
import rs.saga.obuka.sagashop.dto.user.CreateUserCmd;
import rs.saga.obuka.sagashop.dto.user.UpdateUserCmd;
import rs.saga.obuka.sagashop.dto.user.UserInfo;
import rs.saga.obuka.sagashop.dto.user.UserResult;
import rs.saga.obuka.sagashop.exception.ServiceException;
import rs.saga.obuka.sagashop.mapper.UserMapper;
import rs.saga.obuka.sagashop.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserRest {

    private final UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public UserInfo save(@RequestBody @Valid CreateUserCmd cmd) throws ServiceException {
        return UserMapper.INSTANCE.userToUserInfo(userService.save(cmd));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<UserResult> findAll() {
        return userService.findAll();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public UserInfo findById(@PathVariable Long id) throws ServiceException {
        return userService.findById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid UpdateUserCmd cmd) throws ServiceException {
        userService.update(cmd);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws ServiceException {
        userService.delete(id);
    }

}
