package rs.saga.obuka.sagashop.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rs.saga.obuka.sagashop.dao.PayPalAccountDAO;
import rs.saga.obuka.sagashop.dao.UserDAO;
import rs.saga.obuka.sagashop.domain.PayPalAccount;
import rs.saga.obuka.sagashop.domain.User;
import rs.saga.obuka.sagashop.dto.paypalaccount.CreatePayPalAccountCmd;
import rs.saga.obuka.sagashop.dto.paypalaccount.PayPalAccountInfo;
import rs.saga.obuka.sagashop.dto.paypalaccount.PayPalAccountResult;
import rs.saga.obuka.sagashop.dto.paypalaccount.UpdatePayPalAccountCmd;
import rs.saga.obuka.sagashop.exception.DAOException;
import rs.saga.obuka.sagashop.exception.ErrorCode;
import rs.saga.obuka.sagashop.exception.ServiceException;
import rs.saga.obuka.sagashop.exception.ValidationException;
import rs.saga.obuka.sagashop.mapper.PayPalAccountMapper;
import rs.saga.obuka.sagashop.service.PayPalAccountService;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PayPalAccountServiceImpl implements PayPalAccountService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final PayPalAccountDAO payPalAccountDAO;
    private final UserDAO userDAO;

    @Override
    public PayPalAccount save(CreatePayPalAccountCmd cmd) throws ServiceException {

        if (!payPalAccountDAO.isPayPalAccountUnique(cmd.getAccountNumber())) {
            throw new ValidationException(ErrorCode.ERR_GEN_005);
        }

        PayPalAccount payPalAccount = PayPalAccountMapper.INSTANCE.createPayPalAccountCmdToPayPalAccount(cmd);

        User user = userDAO.findOne(cmd.getUserId());
        if (user == null) {
            throw new ServiceException(ErrorCode.ERR_GEN_002, "User does not exist");
        }
        payPalAccount.setUser(user);

        try {
            payPalAccount = payPalAccountDAO.save(payPalAccount);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException(ErrorCode.ERR_GEN_001, "Saving of PayPal Account failed!", e);
        }

        return payPalAccount;
    }

    @Override
    public List<PayPalAccountResult> findAll() {
        return PayPalAccountMapper.INSTANCE.listPayPalAccountToListPayPalAccountResult(payPalAccountDAO.findAll());
    }

    @Override
    public PayPalAccountInfo findById(Long id) {
        return PayPalAccountMapper.INSTANCE.payPalAccountToPayPalAccountInfo(payPalAccountDAO.findOne(id));
    }

    @Override
    public void update(UpdatePayPalAccountCmd cmd) throws ServiceException {

        PayPalAccount payPalAccount;

        try {
            payPalAccount = payPalAccountDAO.findOne(cmd.getId());

            if (payPalAccount == null) {
                throw new ServiceException(ErrorCode.ERR_GEN_002, "PayPal Account does not exist");
            }

            PayPalAccountMapper.INSTANCE.updatePayPalAccountCmdToPayPalAccount(payPalAccount, cmd);
            payPalAccountDAO.merge(payPalAccount);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException(ErrorCode.ERR_GEN_005, "Update of PayPal Account failed!", e);
        }

    }

    @Override
    public void delete(Long id) throws ServiceException {
        PayPalAccount payPalAccount = payPalAccountDAO.findOne(id);

        if (payPalAccount != null) {
            try {
                payPalAccountDAO.delete(payPalAccount);
            } catch (DAOException e) {
                LOGGER.error(e.getMessage());
                throw new ServiceException(ErrorCode.ERR_GEN_003, "Deleting of PayPal Account failed!", e);
            }
        } else {
            throw new ServiceException(ErrorCode.ERR_GEN_002, "PayPal Account does not exist!");
        }

    }
}
