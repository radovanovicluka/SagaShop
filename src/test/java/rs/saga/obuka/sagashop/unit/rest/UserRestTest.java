package rs.saga.obuka.sagashop.unit.rest;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import rs.saga.obuka.sagashop.AbstractUnitRestTest;
import rs.saga.obuka.sagashop.builder.UserBuilder;
import rs.saga.obuka.sagashop.domain.User;
import rs.saga.obuka.sagashop.dto.user.CreateUserCmd;
import rs.saga.obuka.sagashop.dto.user.UpdateUserCmd;
import rs.saga.obuka.sagashop.dto.user.UserInfo;
import rs.saga.obuka.sagashop.dto.user.UserResult;
import rs.saga.obuka.sagashop.rest.UserRest;
import rs.saga.obuka.sagashop.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Tag("REST")
@WebMvcTest(controllers = UserRest.class)
public class UserRestTest extends AbstractUnitRestTest {

    @MockBean
    private UserService userService;

    @Test
    public void saveUser() throws Exception {
        CreateUserCmd cmd = new CreateUserCmd();
        String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cmd);
        User user = UserBuilder.userLuka();

        doReturn(user).when(userService).save(any(CreateUserCmd.class));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInString)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(user.getName()));
    }

    @Test
    public void findAll() throws Exception {
        List<UserResult> results = new ArrayList<>();
        results.add(new UserResult(1L, "User1", "Name 1", "Surname 1"));
        results.add(new UserResult(2L, "User2", "Name 2", "Surname 2"));

        doReturn(results).when(userService).findAll();

        String path = "/user";
        mockMvc.perform(get(path))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]").exists())
                .andExpect(jsonPath("$.[0].id").isNumber())
                .andExpect(jsonPath("$.[0].id").value(results.get(0).getId()))
                .andExpect(jsonPath("$.[0].name").isString())
                .andExpect(jsonPath("$.[0].name").value(results.get(0).getName()))
                .andExpect(jsonPath("$.[1]").exists())
                .andExpect(jsonPath("$.[1].id").isNumber())
                .andExpect(jsonPath("$.[1].id").value(results.get(1).getId()))
                .andExpect(jsonPath("$.[1].name").isString())
                .andExpect(jsonPath("$.[1].name").value(results.get(1).getName()));
    }

    @Test
    public void findById() throws Exception {
        UserInfo info = new UserInfo(1L, "username", "name", "surname");

        doReturn(info).when(userService).findById(anyLong());

        String path = "/user/1";

        mockMvc.perform(get(path))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(info.getId()))
                .andExpect(jsonPath("$.name").isString())
                .andExpect(jsonPath("$.name").value(info.getName()));
    }

    @Test
    public void updateUser() throws Exception {
        UpdateUserCmd cmd = new UpdateUserCmd(1L, "Updated username", "Updated password", "Updated name", "Updated surname");
        String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cmd);

        doNothing().when(userService).update(any(UpdateUserCmd.class));

        mockMvc.perform(put("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInString))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteUser() throws Exception {
        doNothing().when(userService).delete(anyLong());

        mockMvc.perform(delete("/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

}
