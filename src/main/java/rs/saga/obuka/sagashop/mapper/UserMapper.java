package rs.saga.obuka.sagashop.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import rs.saga.obuka.sagashop.domain.User;
import rs.saga.obuka.sagashop.dto.user.CreateUserCmd;
import rs.saga.obuka.sagashop.dto.user.UpdateUserCmd;
import rs.saga.obuka.sagashop.dto.user.UserInfo;
import rs.saga.obuka.sagashop.dto.user.UserResult;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    User createUserCmdToUser(CreateUserCmd cmd);

    @Mapping(source = "password", target = "password")
    User userDetailsToUser(org.springframework.security.core.userdetails.User user);

    List<UserResult> listUserToListUserResult(List<User> users);

    UserInfo userToUserInfo(User user);

    void updateUserCmdToUser(@MappingTarget User user, UpdateUserCmd cmd);

    @AfterMapping
    default void afterMappingDTO(User user, @MappingTarget UserInfo userInfo){
        AuditMapper.INSTANCE.fillAudit(user, userInfo.getAudit());
    }
}
