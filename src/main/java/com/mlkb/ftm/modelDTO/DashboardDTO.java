package com.mlkb.ftm.modelDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Month;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private Double totalBalance;
    private Double difference;
    private List<AccountDTO> popularAccounts;
    private List<ActivityDTO> recentActivities;
    private Map<Month, Double> incomeFunds;
    private Map<Month, Double> expenseFunds;

    public static final class Builder {
        private Double totalBalance;
        private Double difference;
        private List<AccountDTO> popularAccounts;
        private List<ActivityDTO> recentActivities;
        private Map<Month, Double> incomeFunds;
        private Map<Month, Double> expenseFunds;

        public Builder withTotalBalance(Double totalBalance) {
            this.totalBalance = totalBalance;
            return this;
        }

        public Builder withDifference(Double difference) {
            this.difference = difference;
            return this;
        }

        public Builder withPopularAccounts(List<AccountDTO> popularAccounts) {
            this.popularAccounts = popularAccounts;
            return this;
        }

        public Builder withRecentActivities(List<ActivityDTO> activities) {
            this.recentActivities = activities;
            return this;
        }

        public Builder withIncomeFunds(Map<Month, Double> incomeFunds) {
            this.incomeFunds = incomeFunds;
            return this;
        }

        public Builder withExpenseFunds(Map<Month, Double> expenseFunds) {
            this.expenseFunds = expenseFunds;
            return this;
        }

        public DashboardDTO build() {
            DashboardDTO newDashboardDTO = new DashboardDTO();

            newDashboardDTO.totalBalance = this.totalBalance;
            newDashboardDTO.difference = this.difference;
            newDashboardDTO.popularAccounts = this.popularAccounts;
            newDashboardDTO.recentActivities = this.recentActivities;
            newDashboardDTO.incomeFunds = this.incomeFunds;
            newDashboardDTO.expenseFunds = this.expenseFunds;

            return newDashboardDTO;
        }
    }
}
