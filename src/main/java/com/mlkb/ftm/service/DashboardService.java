package com.mlkb.ftm.service;

import com.mlkb.ftm.entity.Account;
import com.mlkb.ftm.entity.User;
import com.mlkb.ftm.modelDTO.DashboardDTO;
import com.mlkb.ftm.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DashboardService {
    private UserRepository userRepository;

    public DashboardService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Optional<DashboardDTO> getDashboard(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        Optional<DashboardDTO> optionalDashboardDTO = Optional.empty();
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Double totalBalance = getTotalBalance(user);
            DashboardDTO dashboardDTO = new DashboardDTO(totalBalance);
            optionalDashboardDTO = Optional.of(dashboardDTO);
        }
        return optionalDashboardDTO;
    }

    private Double getTotalBalance(User user){
        Double totalBalance = user.getAccounts().stream()
                .mapToDouble(Account::getCurrentBalance)
                .reduce(0, Double::sum);
        return totalBalance;
    }
}
