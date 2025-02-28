package rs.saga.obuka.sagashop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import rs.saga.obuka.sagashop.domain.PayPalAccount;
import rs.saga.obuka.sagashop.dto.paypalaccount.CreatePayPalAccountCmd;
import rs.saga.obuka.sagashop.dto.paypalaccount.PayPalAccountInfo;
import rs.saga.obuka.sagashop.dto.paypalaccount.PayPalAccountResult;
import rs.saga.obuka.sagashop.dto.paypalaccount.UpdatePayPalAccountCmd;

import java.util.List;

@Mapper
public interface PayPalAccountMapper {

    PayPalAccountMapper INSTANCE = Mappers.getMapper(PayPalAccountMapper.class);

    @Mapping(target = "id", ignore = true)
    PayPalAccount createPayPalAccountCmdToPayPalAccount(CreatePayPalAccountCmd cmd);

    List<PayPalAccountResult> listPayPalAccountToListPayPalAccountResult(List<PayPalAccount> categories);

    PayPalAccountInfo payPalAccountToPayPalAccountInfo(PayPalAccount payPalAccount);

    void updatePayPalAccountCmdToPayPalAccount(@MappingTarget PayPalAccount payPalAccount, UpdatePayPalAccountCmd cmd);

}
