package com.mlkb.ftm.modelDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private Double totalBalance;
    private Double difference;
    private List<AccountDTO> popularAccounts;
    private List<ActivityDTO> recentActivities;
}
