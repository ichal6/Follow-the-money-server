package com.mlkb.ftm.fixture;

import com.mlkb.ftm.entity.Payee;

public class PayeeEntityFixture {
    public static Payee MariuszTransKomis() {
        var payee = new Payee();
        payee.setName("Mariusz-trans komis");
        payee.setId(4L);
        payee.setIsEnabled(true);

        return payee;
    }

    public static Payee SuperTaxi() {
        var payee = new Payee();
        payee.setName("Super Taxi");
        payee.setId(5L);
        payee.setIsEnabled(true);

        return payee;
    }
}
