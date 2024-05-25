package com.mlkb.ftm.fixture;

import com.mlkb.ftm.modelDTO.NewAccountDTO;

public class NewAccountDTOFixture {
    public static NewAccountDTO milleniumNewAccountDTO() {
        var accountDTO = new NewAccountDTO();
        accountDTO.setId(1L);
        accountDTO.setUserEmail("example@user.pl");
        return accountDTO;
    }
}
