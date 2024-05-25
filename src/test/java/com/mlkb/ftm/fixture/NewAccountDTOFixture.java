package com.mlkb.ftm.fixture;

import com.mlkb.ftm.entity.AccountType;
import com.mlkb.ftm.modelDTO.NewAccountDTO;

public class NewAccountDTOFixture {
    public static NewAccountDTO millenniumNewAccountDTO() {
        var accountDTO = new NewAccountDTO();
        accountDTO.setId(1L);
        accountDTO.setName("Millennium");
        accountDTO.setUserEmail("user@user.pl");
        accountDTO.setStartingBalance(0.0);
        accountDTO.setCurrentBalance(200000.0);
        accountDTO.setAccountType(AccountType.BANK.toString());
        return accountDTO;
    }
}
