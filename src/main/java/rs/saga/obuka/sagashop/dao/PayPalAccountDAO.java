package rs.saga.obuka.sagashop.dao;

import rs.saga.obuka.sagashop.domain.PayPalAccount;

public interface PayPalAccountDAO extends AbstractDAO<PayPalAccount, Long> {

    boolean isPayPalAccountUnique(String payPalAccount);

}
