package rs.saga.obuka.sagashop.integration.service;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import rs.saga.obuka.sagashop.AbstractIntegrationTest;
import rs.saga.obuka.sagashop.domain.Address;
import rs.saga.obuka.sagashop.domain.PayPalAccount;
import rs.saga.obuka.sagashop.domain.User;
import rs.saga.obuka.sagashop.dto.paypalaccount.CreatePayPalAccountCmd;
import rs.saga.obuka.sagashop.dto.paypalaccount.PayPalAccountInfo;
import rs.saga.obuka.sagashop.dto.paypalaccount.PayPalAccountResult;
import rs.saga.obuka.sagashop.dto.paypalaccount.UpdatePayPalAccountCmd;
import rs.saga.obuka.sagashop.dto.user.CreateUserCmd;
import rs.saga.obuka.sagashop.exception.ServiceException;
import rs.saga.obuka.sagashop.service.PayPalAccountService;
import rs.saga.obuka.sagashop.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Service")
public class PayPalAccountServiceTest extends AbstractIntegrationTest {

    @Autowired
    private PayPalAccountService payPalAccountService;

    @Autowired
    private UserService userService;

    @Test
    public void savePayPalAccount() throws ServiceException {

        CreateUserCmd cmd1 = new CreateUserCmd("radovan", "password", "Luka", "R");

        User user = userService.save(cmd1);

        CreatePayPalAccountCmd cmd2 = new CreatePayPalAccountCmd("87646313", new BigDecimal(5498.13),
                "SRB", LocalDate.now(), new Address(), user.getId());
        PayPalAccount payPalAccount = payPalAccountService.save(cmd2);

        assertNotNull(payPalAccount);
        assertNotNull(payPalAccount.getId());
        assertNotNull(payPalAccount.getUser());
        assertEquals(payPalAccount.getUser().getUsername(), "radovan");
        assertEquals(cmd2.getAccountNumber(), payPalAccount.getAccountNumber());
    }

    @Test
    public void updatePayPalAccount() throws ServiceException {

        CreateUserCmd cmd1 = new CreateUserCmd("radovan", "password", "Luka", "R");

        User user = userService.save(cmd1);

        CreatePayPalAccountCmd cmd2 = new CreatePayPalAccountCmd("87646313", new BigDecimal(5498.13),
                "SRB", LocalDate.now(), new Address(), user.getId());
        PayPalAccount payPalAccount = payPalAccountService.save(cmd2);

        assertNotNull(payPalAccount);
        assertNotNull(payPalAccount.getId());

        UpdatePayPalAccountCmd update = new UpdatePayPalAccountCmd(payPalAccount.getId(), payPalAccount.getAccountNumber(),
                payPalAccount.getBudget(), payPalAccount.getLanguage(), payPalAccount.getExpiresOn(), payPalAccount.getBillingAddress());
        update.setLanguage("ENG");
        payPalAccountService.update(update);

        PayPalAccountInfo info = payPalAccountService.findById(payPalAccount.getId());
        assertNotNull(info);
        assertEquals(payPalAccount.getAccountNumber(), info.getAccountNumber());
        assertEquals(payPalAccount.getId(), info.getId());
        assertEquals("ENG", info.getLanguage());

    }

    @Test
    public void deletePayPalAccount() throws ServiceException {

        CreateUserCmd cmd1 = new CreateUserCmd("radovan", "password", "Luka", "R");

        User user = userService.save(cmd1);

        CreatePayPalAccountCmd cmd2 = new CreatePayPalAccountCmd("87646313", new BigDecimal(5498.13),
                "SRB", LocalDate.now(), new Address(), user.getId());
        PayPalAccount payPalAccount = payPalAccountService.save(cmd2);

        assertNotNull(payPalAccount);
        assertNotNull(payPalAccount.getId());

        payPalAccountService.delete(payPalAccount.getId());

        PayPalAccountInfo info = payPalAccountService.findById(payPalAccount.getId());
        assertNull(info);
    }

    @Test
    public void findOne() throws ServiceException {

        CreateUserCmd cmd1 = new CreateUserCmd("radovan", "password", "Luka", "R");

        User user = userService.save(cmd1);

        CreatePayPalAccountCmd cmd2 = new CreatePayPalAccountCmd("87646313", new BigDecimal(5498.13),
                "SRB", LocalDate.now(), new Address(), user.getId());
        PayPalAccount payPalAccount = payPalAccountService.save(cmd2);
        assertNotNull(payPalAccount);
        assertNotNull(payPalAccount.getId());

        PayPalAccountInfo info = payPalAccountService.findById(payPalAccount.getId());

        assertNotNull(info);
        assertEquals(payPalAccount.getAccountNumber(), info.getAccountNumber());
    }

    @Test
    public void findAll() throws ServiceException {
        CreateUserCmd cmd1 = new CreateUserCmd("radovan", "password", "Luka", "R");

        User user = userService.save(cmd1);

        CreatePayPalAccountCmd cmd2 = new CreatePayPalAccountCmd("87646313", new BigDecimal(5498.13),
                "SRB", LocalDate.now(), new Address(), user.getId());
        PayPalAccount payPalAccount1 = payPalAccountService.save(cmd2);

        assertNotNull(payPalAccount1);
        assertNotNull(payPalAccount1.getId());

        cmd1 = new CreateUserCmd("urosr", "password", "Uros", "R");

        user = userService.save(cmd1);

        cmd2 = new CreatePayPalAccountCmd("9846131", new BigDecimal(8945.25),
                "SRB", LocalDate.now(), new Address(), user.getId());
        PayPalAccount payPalAccount2 = payPalAccountService.save(cmd2);

        assertNotNull(payPalAccount2);
        assertNotNull(payPalAccount2.getId());

        List<PayPalAccountResult> results = payPalAccountService.findAll();
        assertNotNull(results);
        assertTrue(results.stream().anyMatch(e -> e.getAccountNumber().equals("87646313")));
        assertTrue(results.stream().anyMatch(e -> e.getAccountNumber().equals("9846131")));

    }

}
