package rs.saga.obuka.sagashop.exception;

import java.math.BigDecimal;

public class BudgetExceededException extends RuntimeException {

    private BigDecimal amount;

    public BudgetExceededException(BigDecimal amount) {
        super("Budget exceeded by: " + amount);
    }

    public BudgetExceededException() {
        super("Budget exceeded");
    }

}
