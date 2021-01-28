package com.mlkb.ftm.controller;

import com.mlkb.ftm.exception.InputIncorrectException;
import com.mlkb.ftm.modelDTO.NewAccountDTO;
import com.mlkb.ftm.modelDTO.PaymentDTO;
import com.mlkb.ftm.modelDTO.TransactionDTO;
import com.mlkb.ftm.modelDTO.TransferDTO;
import com.mlkb.ftm.service.PaymentService;
import com.mlkb.ftm.validation.AccessValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;
    private final AccessValidator accessValidator;

    public PaymentController(PaymentService paymentService, AccessValidator accessValidator) {
        this.paymentService = paymentService;
        this.accessValidator = accessValidator;
    }

    @GetMapping("/{email}")
    public ResponseEntity<Object> getTransactions(@PathVariable("email") String email,
                                                  @RequestParam("id") String accountId,
                                                  @RequestParam("period") String period) {
        accessValidator.checkPermit(email);
        List<PaymentDTO> paymentsDTOList = paymentService.getPaymentsWithParameters(email, accountId, period);
        return new ResponseEntity<>(paymentsDTOList, HttpStatus.OK);
    }

    @PostMapping("/transaction/{email}")
    public ResponseEntity<Object> createTransaction(@PathVariable("email") String email,
                                                    @RequestBody TransactionDTO transactionDTO) throws InputIncorrectException {
        accessValidator.checkPermit(email);
        paymentService.isValidNewTransaction(transactionDTO);
        paymentService.createNewTransaction(transactionDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/transfer/{email}")
    public ResponseEntity<Object> createTransfer(@PathVariable("email") String email,
                                                 @RequestBody TransferDTO transferDTO) throws InputIncorrectException {
        accessValidator.checkPermit(email);
        paymentService.isValidNewTransfer(transferDTO);
        paymentService.createNewTransfer(transferDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/transaction/{id}")
    public ResponseEntity<Object> deleteTransaction(@PathVariable("id") Long idTransaction,
                                                    @CookieValue(value = "e-mail", defaultValue = "none") String email){
        accessValidator.checkPermit(email);
        paymentService.removeTransaction(idTransaction, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
