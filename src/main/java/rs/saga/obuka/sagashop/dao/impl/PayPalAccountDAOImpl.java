package rs.saga.obuka.sagashop.dao.impl;

import org.springframework.stereotype.Repository;
import rs.saga.obuka.sagashop.dao.PayPalAccountDAO;
import rs.saga.obuka.sagashop.domain.PayPalAccount;

@Repository
public class PayPalAccountDAOImpl extends AbstractDAOImpl<PayPalAccount, Long> implements PayPalAccountDAO {
}
