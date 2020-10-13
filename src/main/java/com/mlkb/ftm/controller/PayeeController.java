package com.mlkb.ftm.controller;

import com.mlkb.ftm.modelDTO.PayeeDTO;
import com.mlkb.ftm.service.PayeeService;
import com.mlkb.ftm.validation.AccessValidator;
import com.mlkb.ftm.validation.InputValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/payee")
public class PayeeController {
    private final PayeeService payeeService;
    private final AccessValidator accessValidator;
    private final InputValidator inputValidator;

    PayeeController(PayeeService payeeService,
                    AccessValidator accessValidator,
                    InputValidator inputValidator){
        this.payeeService = payeeService;
        this.accessValidator = accessValidator;
        this.inputValidator = inputValidator;
    }

    @GetMapping("/income/{email}")
    public ResponseEntity<Object> getPayeeForIncome(@PathVariable String email){
        accessValidator.checkPermit(email);
        Set<PayeeDTO> payeesDTO = payeeService.getPayeeForIncome(email);
        return new ResponseEntity<>(payeesDTO, HttpStatus.OK);
    }

    @GetMapping("/expense/{email}")
    public ResponseEntity<Object> getPayeeForExpense(@PathVariable String email){
        accessValidator.checkPermit(email);
        Set<PayeeDTO> payeesDTO = payeeService.getPayeeForExpense(email);
        return new ResponseEntity<>(payeesDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{email}/{id}")
    public ResponseEntity<Object> deleteSubcategory(@PathVariable String email, @PathVariable Long id) {
        accessValidator.checkPermit(email);
        payeeService.deletePayee(email, id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
