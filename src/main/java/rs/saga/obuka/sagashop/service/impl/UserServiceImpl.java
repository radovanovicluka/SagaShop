package rs.saga.obuka.sagashop.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.saga.obuka.sagashop.dao.RoleDAO;
import rs.saga.obuka.sagashop.dao.UserDAO;
import rs.saga.obuka.sagashop.domain.Role;
import rs.saga.obuka.sagashop.domain.User;
import rs.saga.obuka.sagashop.dto.user.CreateUserCmd;
import rs.saga.obuka.sagashop.dto.user.UpdateUserCmd;
import rs.saga.obuka.sagashop.dto.user.UserInfo;
import rs.saga.obuka.sagashop.dto.user.UserResult;
import rs.saga.obuka.sagashop.exception.DAOException;
import rs.saga.obuka.sagashop.exception.ErrorCode;
import rs.saga.obuka.sagashop.exception.ServiceException;
import rs.saga.obuka.sagashop.mapper.UserMapper;
import rs.saga.obuka.sagashop.service.UserService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final PasswordEncoder encoder;

    @Override
    public User save(CreateUserCmd cmd) throws ServiceException {

        User user = UserMapper.INSTANCE.createUserCmdToUser(cmd);

        user.setPassword(encoder.encode(cmd.getPassword()));

        Optional<Role> role = roleDAO.findByName("USER");

        if (role.isEmpty()) {
            try {
                user.getRoles().add(roleDAO.save(new Role("USER")));
            } catch (DAOException e) {
                throw new ServiceException(ErrorCode.ERR_GEN_001, e.getMessage());
            }
        } else {
            user.getRoles().add(role.get());
        }

        try {
            user = userDAO.save(user);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException(ErrorCode.ERR_GEN_001, "Saving of user failed!", e);
        }

        return user;
    }

    @Override
    public List<UserResult> findAll() {
        return UserMapper.INSTANCE.listUserToListUserResult(userDAO.findAll());
    }

    @Override
    public UserInfo findById(Long id) {
        return UserMapper.INSTANCE.userToUserInfo(userDAO.findOne(id));
    }

    @Override
    public void update(UpdateUserCmd cmd) throws ServiceException {
        User user;

        try {
            user = userDAO.findOne(cmd.getId());

            if (user == null) {
                throw new ServiceException(ErrorCode.ERR_GEN_002, "User not found! ID = " + cmd.getId());
            }

            UserMapper.INSTANCE.updateUserCmdToUser(user, cmd);
            userDAO.merge(user);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException(ErrorCode.ERR_GEN_001, "Update of user failed!", e);
        }
    }

    @Override
    public void delete(Long id) throws ServiceException {
        User user = userDAO.findOne(id);

        if (user != null) {
            try {
                userDAO.delete(user);
            } catch (DAOException e) {
                LOGGER.error(e.getMessage());
                throw new ServiceException(ErrorCode.ERR_GEN_001, "Deleting of user failed!", e);
            }
        } else {
            throw new ServiceException(ErrorCode.ERR_GEN_002, "User does not exist!");
        }
    }

    @Override
    public void addRole(Long id, Role role) throws ServiceException {

        User user = userDAO.findOne(id);

        if (user == null) {
            throw new ServiceException(ErrorCode.ERR_GEN_002, "User not found! id = " + id);
        }

        Optional<Role> r = roleDAO.findByName(role.getName());

        if (r.isEmpty()) {
            try {
                user.addRole(roleDAO.save(new Role(role.getName())));
            } catch (DAOException e) {
                throw new ServiceException(ErrorCode.ERR_GEN_001, e.getMessage());
            }
        } else {
            user.getRoles().add(r.get());
        }

        try {
            userDAO.merge(user);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }
}
