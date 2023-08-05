package com.mlkb.ftm.service;

import com.mlkb.ftm.entity.Payee;
import com.mlkb.ftm.modelDTO.PayeeDTO;
import com.mlkb.ftm.repository.PayeeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PayeeService {
    private final PayeeRepository payeeRepository;

    public PayeeService(PayeeRepository payeeRepository){
        this.payeeRepository = payeeRepository;
    }

    public Set<PayeeDTO> getAllPayees(String email) {
        return payeeRepository.getPayees(email).stream()
                .filter(payee -> payee.getIsEnabled().equals(true))
                .map(payee -> new PayeeDTO(payee.getId(), payee.getName()))
                .collect(Collectors.toSet());    }

    public void deletePayee(String email, Long id){
        Optional<Payee> payee = payeeRepository.getPayees(email)
                .stream()
                .filter(findPayee -> findPayee.getId().equals(id))
                .findFirst();
        if (payee.isPresent()){
            payeeRepository.setDisabled(id);
        }
    }

    public void savePayee(PayeeDTO payee, Long userId){
        payeeRepository.addPayee(payee.getName(), userId);
    }

    public void updatePayee(String payeeName, Long payeeID){
        payeeRepository.updatePayee(payeeName, payeeID);
    }
}
