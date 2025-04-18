package rs.saga.obuka.sagashop.integration.service;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import rs.saga.obuka.sagashop.AbstractIntegrationTest;
import rs.saga.obuka.sagashop.domain.User;
import rs.saga.obuka.sagashop.dto.user.CreateUserCmd;
import rs.saga.obuka.sagashop.dto.user.UpdateUserCmd;
import rs.saga.obuka.sagashop.dto.user.UserInfo;
import rs.saga.obuka.sagashop.dto.user.UserResult;
import rs.saga.obuka.sagashop.exception.ServiceException;
import rs.saga.obuka.sagashop.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Service")
public class UserServiceTest extends AbstractIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    public void saveUser() throws ServiceException {

        CreateUserCmd cmd = new CreateUserCmd("radovan", "password", "Luka", "R");
        User user = userService.save(cmd);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals("radovan", user.getUsername());
        assertNotNull(user.getCreationDate());
        assertNotNull(user.getCreatedBy());
        assertNotNull(user.getLastModifiedBy());
        assertNotNull(user.getLastModifiedDate());

    }

    @Test
    public void updateUser() throws ServiceException {

        CreateUserCmd cmd = new CreateUserCmd("radovan", "password", "Luka", "R");
        User user = userService.save(cmd);
        assertNotNull(user);
        assertNotNull(user.getId());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        UpdateUserCmd update = new UpdateUserCmd(user.getId(), user.getUsername(), user.getPassword(), user.getName(),
                user.getSurname());
        update.setName("Uros");
        userService.update(update);

        UserInfo info = userService.findById(user.getId());
        assertNotNull(info);
        assertEquals("Uros", info.getName());
        assertNotNull(info.getAudit().getCreationDate());
        assertNotNull(info.getAudit().getCreatedBy());
        assertNotNull(info.getAudit().getLastModifiedBy());
        assertNotNull(info.getAudit().getLastModifiedDate());
        assertNotEquals(info.getAudit().getCreationDate(), info.getAudit().getLastModifiedDate());
    }

    @Test
    public void deleteUser() throws ServiceException {

        CreateUserCmd cmd = new CreateUserCmd("radovan", "password", "Luka", "R");
        User user = userService.save(cmd);
        assertNotNull(user);
        assertNotNull(user.getId());

        userService.delete(user.getId());

        UserInfo info = userService.findById(user.getId());
        assertNull(info);
    }

    @Test
    public void findOne() throws ServiceException {

        CreateUserCmd cmd = new CreateUserCmd("radovan", "password", "Luka", "R");
        User user = userService.save(cmd);
        assertNotNull(user);
        assertNotNull(user.getId());

        UserInfo info = userService.findById(user.getId());
        assertNotNull(info);
        assertEquals(user.getId(), info.getId());
        assertEquals("radovan", info.getUsername());
        assertEquals("Luka", info.getName());
    }

    @Test
    public void findAll() throws ServiceException {

        CreateUserCmd cmd1 = new CreateUserCmd("radovan", "password", "Luka", "R");
        User user1 = userService.save(cmd1);
        assertNotNull(user1);
        assertNotNull(user1.getId());

        CreateUserCmd cmd2 = new CreateUserCmd("urosr", "password", "Uros", "R");
        User user2 = userService.save(cmd2);
        assertNotNull(user2);
        assertNotNull(user2.getId());

        List<UserResult> results = userService.findAll();
        assertEquals(5, results.size());
        assertTrue(results.stream().anyMatch(e -> e.getName().equals("Uros")));
        assertTrue(results.stream().anyMatch(e -> e.getName().equals("Luka")));
    }

}
