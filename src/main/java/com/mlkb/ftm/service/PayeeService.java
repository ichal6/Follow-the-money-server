package com.mlkb.ftm.service;

import com.mlkb.ftm.entity.GeneralType;
import com.mlkb.ftm.entity.Payee;
import com.mlkb.ftm.modelDTO.PayeeDTO;
import com.mlkb.ftm.repository.PayeesRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
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
                .filter(payee -> payee.getIsEnabled().equals(true))
                .map(payee -> new PayeeDTO(payee.getId(), payee.getName(), payee.getGeneralType()))
                .collect(Collectors.toSet());
    }

    public Set<PayeeDTO> getPayeeForExpense(String email) {
        return payeesRepository.getPayees(email).stream()
                .filter(payee -> payee.getGeneralType() == GeneralType.EXPENSE)
                .filter(payee -> payee.getIsEnabled().equals(true))
                .map(payee -> new PayeeDTO(payee.getId(), payee.getName(), payee.getGeneralType()))
                .collect(Collectors.toSet());
    }

    public void deletePayee(String email, Long id){
        Optional<Payee> payee = payeesRepository.getPayees(email)
                .stream()
                .filter(findPayee -> findPayee.getId().equals(id))
                .findFirst();
        if (payee.isPresent()){
            payeesRepository.setDisabled(id);
        }
    }

    public void savePayee(PayeeDTO payee, Long userId){
        payeesRepository.addPayee(payee.getType().toString(), payee.getName(), userId);
    }
}
