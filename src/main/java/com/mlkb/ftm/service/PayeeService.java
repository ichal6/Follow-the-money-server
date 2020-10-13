package com.mlkb.ftm.service;

import com.mlkb.ftm.entity.GeneralType;
import com.mlkb.ftm.modelDTO.PayeeDTO;
import com.mlkb.ftm.repository.PayeesRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PayeeService {
    private final PayeesRepository payeesRepository;

    public PayeeService(PayeesRepository payeesRepository){
        this.payeesRepository = payeesRepository;
    }

    public Set<PayeeDTO> getPayeeForIncome(String email) {
        return payeesRepository.getPayees(email).stream()
                .filter(payee -> payee.getGeneralType() == GeneralType.INCOME)
                .map(payee -> new PayeeDTO(payee.getId(), payee.getName(), payee.getGeneralType()))
                .collect(Collectors.toSet());
    }

    public Set<PayeeDTO> getPayeeForExpense(String email) {
        return payeesRepository.getPayees(email).stream()
                .filter(payee -> payee.getGeneralType() == GeneralType.EXPENSE)
                .map(payee -> new PayeeDTO(payee.getId(), payee.getName(), payee.getGeneralType()))
                .collect(Collectors.toSet());
    }
}
