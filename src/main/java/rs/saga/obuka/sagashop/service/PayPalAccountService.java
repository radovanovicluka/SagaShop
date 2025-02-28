package rs.saga.obuka.sagashop.service;

import rs.saga.obuka.sagashop.domain.PayPalAccount;
import rs.saga.obuka.sagashop.dto.paypalaccount.PayPalAccountInfo;
import rs.saga.obuka.sagashop.dto.paypalaccount.PayPalAccountResult;
import rs.saga.obuka.sagashop.dto.paypalaccount.CreatePayPalAccountCmd;
import rs.saga.obuka.sagashop.dto.paypalaccount.UpdatePayPalAccountCmd;
import rs.saga.obuka.sagashop.exception.ServiceException;

import java.util.List;

public interface PayPalAccountService {

    PayPalAccount save(CreatePayPalAccountCmd cmd) throws ServiceException;

    List<PayPalAccountResult> findAll();

    PayPalAccountInfo findById(Long id);

    void update(UpdatePayPalAccountCmd PayPalAccountDTO) throws ServiceException;

    void delete(Long id) throws ServiceException;

}
