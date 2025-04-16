package rs.saga.obuka.sagashop.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.saga.obuka.sagashop.domain.PayPalAccount;
import rs.saga.obuka.sagashop.dto.paypalaccount.CreatePayPalAccountCmd;
import rs.saga.obuka.sagashop.dto.paypalaccount.PayPalAccountInfo;
import rs.saga.obuka.sagashop.dto.paypalaccount.PayPalAccountResult;
import rs.saga.obuka.sagashop.dto.paypalaccount.UpdatePayPalAccountCmd;
import rs.saga.obuka.sagashop.exception.ServiceException;
import rs.saga.obuka.sagashop.mapper.PayPalAccountMapper;
import rs.saga.obuka.sagashop.service.PayPalAccountService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/paypalaccount")
public class PayPalAccountRest {

    private final PayPalAccountService payPalAccountService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public PayPalAccountInfo save(@RequestBody @Valid CreatePayPalAccountCmd cmd) throws ServiceException {
        return PayPalAccountMapper.INSTANCE.payPalAccountToPayPalAccountInfo(payPalAccountService.save(cmd));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<PayPalAccountResult> findAll() {
        return payPalAccountService.findAll();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public PayPalAccountInfo findById(@PathVariable Long id) throws ServiceException {
        return payPalAccountService.findById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid UpdatePayPalAccountCmd cmd) throws ServiceException {
        payPalAccountService.update(cmd);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws ServiceException {
        payPalAccountService.delete(id);
    }

}
