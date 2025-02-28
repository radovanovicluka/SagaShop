package rs.saga.obuka.sagashop.unit;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import rs.saga.obuka.sagashop.AbstractUnitRestTest;
import rs.saga.obuka.sagashop.builder.PayPalAccountBuilder;
import rs.saga.obuka.sagashop.domain.Address;
import rs.saga.obuka.sagashop.domain.PayPalAccount;
import rs.saga.obuka.sagashop.dto.paypalaccount.CreatePayPalAccountCmd;
import rs.saga.obuka.sagashop.dto.paypalaccount.PayPalAccountInfo;
import rs.saga.obuka.sagashop.dto.paypalaccount.PayPalAccountResult;
import rs.saga.obuka.sagashop.dto.paypalaccount.UpdatePayPalAccountCmd;
import rs.saga.obuka.sagashop.dto.user.UpdateUserCmd;
import rs.saga.obuka.sagashop.dto.user.UserInfo;
import rs.saga.obuka.sagashop.dto.user.UserResult;
import rs.saga.obuka.sagashop.rest.PayPalAccountRest;
import rs.saga.obuka.sagashop.service.PayPalAccountService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Tag("REST")
@WebMvcTest(controllers = PayPalAccountRest.class)
public class PayPalAccountRestTest extends AbstractUnitRestTest {

    @MockBean
    private PayPalAccountService payPalAccountService;

    @Test
    public void savePayPalAccount() throws Exception {
        CreatePayPalAccountCmd cmd = new CreatePayPalAccountCmd();
        String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cmd);
        PayPalAccount payPalAccount = PayPalAccountBuilder.account1Build();

        doReturn(payPalAccount).when(payPalAccountService).save(any(CreatePayPalAccountCmd.class));

        mockMvc.perform(post("/paypalaccount/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInString)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value(payPalAccount.getAccountNumber()));
    }

    @Test
    public void findAll() throws Exception {
        List<PayPalAccountResult> results = new ArrayList<>();
        results.add(new PayPalAccountResult(1L, "89653131684", new BigDecimal(100.12),
                "SRB", LocalDate.now(), new Address()));
        results.add(new PayPalAccountResult(2L, "7498751319", new BigDecimal(8946.64),
                "ENG", LocalDate.now(), new Address()));

        doReturn(results).when(payPalAccountService).findAll();

        String path = "/paypalaccount/all";
        mockMvc.perform(get(path))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]").exists())
                .andExpect(jsonPath("$.[0].id").isNumber())
                .andExpect(jsonPath("$.[0].id").value(results.get(0).getId()))
                .andExpect(jsonPath("$.[0].accountNumber").isString())
                .andExpect(jsonPath("$.[0].accountNumber").value(results.get(0).getAccountNumber()))
                .andExpect(jsonPath("$.[1]").exists())
                .andExpect(jsonPath("$.[1].id").isNumber())
                .andExpect(jsonPath("$.[1].id").value(results.get(1).getId()))
                .andExpect(jsonPath("$.[1].accountNumber").isString())
                .andExpect(jsonPath("$.[1].accountNumber").value(results.get(1).getAccountNumber()));
    }

    @Test
    public void findById() throws Exception {
        PayPalAccountInfo info = new PayPalAccountInfo(1L, "89653131684", new BigDecimal(100.12),
                "SRB", LocalDate.now(), new Address());

        doReturn(info).when(payPalAccountService).findById(anyLong());

        String path = "/paypalaccount/id/1";

        mockMvc.perform(get(path))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(info.getId()))
                .andExpect(jsonPath("$.accountNumber").isString())
                .andExpect(jsonPath("$.accountNumber").value(info.getAccountNumber()));
    }

    @Test
    public void updateCategory() throws Exception {
        UpdatePayPalAccountCmd cmd = new UpdatePayPalAccountCmd(1L, "89653131684",
                new BigDecimal(100.12), "SRB", LocalDate.now(), new Address());
        String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cmd);

        doNothing().when(payPalAccountService).update(any(UpdatePayPalAccountCmd.class));

        mockMvc.perform(put("/paypalaccount/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInString))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deletePayPalAccount() throws Exception {
        doNothing().when(payPalAccountService).delete(anyLong());

        mockMvc.perform(delete("/paypalaccount/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

}
