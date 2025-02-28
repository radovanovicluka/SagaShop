package rs.saga.obuka.sagashop.dao.impl;

import org.springframework.stereotype.Repository;
import rs.saga.obuka.sagashop.dao.PayPalAccountDAO;
import rs.saga.obuka.sagashop.domain.PayPalAccount;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class PayPalAccountDAOImpl extends AbstractDAOImpl<PayPalAccount, Long> implements PayPalAccountDAO {
    @Override
    public boolean isPayPalAccountUnique(String accountNumber) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<PayPalAccount> query = cb.createQuery(PayPalAccount.class);
        Root<PayPalAccount> payPalAccount = query.from(PayPalAccount.class);
        query.select(payPalAccount).where(cb.equal((payPalAccount.get("accountNumber")), accountNumber));

        return entityManager.createQuery(query).getResultList().isEmpty();
    }
}
