package com.mlkb.ftm.modelDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Month;
import java.util.List;
import java.util.TreeMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private Double totalBalance;
    private Double difference;
    private List<AccountDTO> popularAccounts;
    private List<ActivityDTO> recentActivities;
    private TreeMap<Month, Double> incomeFunds;
    private TreeMap<Month, Double> expenseFunds;
}
