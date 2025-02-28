package rs.saga.obuka.sagashop.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rs.saga.obuka.sagashop.domain.PayPalAccount;
import rs.saga.obuka.sagashop.dto.paypalaccount.CreatePayPalAccountCmd;
import rs.saga.obuka.sagashop.dto.paypalaccount.PayPalAccountInfo;
import rs.saga.obuka.sagashop.dto.paypalaccount.PayPalAccountResult;
import rs.saga.obuka.sagashop.dto.paypalaccount.UpdatePayPalAccountCmd;
import rs.saga.obuka.sagashop.exception.ServiceException;
import rs.saga.obuka.sagashop.service.PayPalAccountService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/paypalaccount")
public class PayPalAccountRest {

    private final PayPalAccountService payPalAccountService;

    @Autowired
    public PayPalAccountRest(PayPalAccountService payPalAccountService) {
        this.payPalAccountService = payPalAccountService;
    }

    @PostMapping
    public PayPalAccount save(@RequestBody @Valid CreatePayPalAccountCmd cmd ) throws ServiceException {
        return payPalAccountService.save(cmd);
    }

    @GetMapping
    @ResponseBody
    public List<PayPalAccountResult> findAll() {
        return payPalAccountService.findAll();
    }

    @GetMapping("/{id}")
    public PayPalAccountInfo findById(@PathVariable Long id ) throws ServiceException {
        return payPalAccountService.findById(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid UpdatePayPalAccountCmd cmd) throws ServiceException {
        payPalAccountService.update(cmd);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws ServiceException {
        payPalAccountService.delete(id);
    }

}
