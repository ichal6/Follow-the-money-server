package com.mlkb.ftm.controller;

import com.mlkb.ftm.exception.InputIncorrectException;
import com.mlkb.ftm.modelDTO.PaymentDTO;
import com.mlkb.ftm.modelDTO.TransactionDTO;
import com.mlkb.ftm.modelDTO.TransferDTO;
import com.mlkb.ftm.service.PaymentService;
import com.mlkb.ftm.validation.AccessValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;
    private final AccessValidator accessValidator;

    public PaymentController(PaymentService paymentService, AccessValidator accessValidator) {
        this.paymentService = paymentService;
        this.accessValidator = accessValidator;
    }

    @GetMapping( value ="/{email}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Get all transaction for user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation")
    })
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getTransactions(@PathVariable("email") String email,
                                                  @RequestParam("id") Optional<String> possibleAccountId,
                                                  @RequestParam("period") Optional<String> possiblePeriod) {
        accessValidator.checkPermit(email);
        List<PaymentDTO> paymentsDTOList;
        if (possibleAccountId.isEmpty() && possiblePeriod.isEmpty()){
            paymentsDTOList = paymentService.getPayments(email);
        } else if (possibleAccountId.isPresent() && possiblePeriod.isPresent()){
            paymentsDTOList =
                    paymentService.getPaymentsWithParameters(email, possibleAccountId.get(), possiblePeriod.get());
        } else if (possibleAccountId.isPresent()){
            paymentsDTOList = paymentService.getPaymentsWithAccount(email, possibleAccountId.get());
        } else {
            paymentsDTOList = paymentService.getPaymentsForPeriod(email, possiblePeriod.get());
        }
        return new ResponseEntity<>(paymentsDTOList, HttpStatus.OK);
    }

    @GetMapping("/transaction/{email}/{id}")
    @Operation(summary = "Get a single transaction by id")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable String email, @PathVariable Long id) {
        this.accessValidator.checkPermit(email);

        TransactionDTO transactionDTO = this.paymentService.getTransaction(email, id);

        return new ResponseEntity<>(transactionDTO, HttpStatus.OK);
    }

    @GetMapping("/transfer/{email}/{id}")
    @Operation(summary = "Get a single transfer by id")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TransferDTO> getTransfer(@PathVariable String email, @PathVariable Long id) {
        this.accessValidator.checkPermit(email);

        TransferDTO transferDTO = this.paymentService.getTransfer(email, id);

        return new ResponseEntity<>(transferDTO, HttpStatus.OK);
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

    @DeleteMapping("/transfer/{id}")
    public ResponseEntity<Object> deleteTransfer(@PathVariable("id") Long idTransfer,
                                                    @CookieValue(value = "e-mail", defaultValue = "none") String email){
        accessValidator.checkPermit(email);
        paymentService.removeTransfer(idTransfer, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/transaction")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update transaction")
    public ResponseEntity<?> updateTransaction(@RequestBody TransactionDTO updateTransaction,
                                                    @CookieValue(value = "e-mail", defaultValue = "none") String email)
            throws InputIncorrectException {
        accessValidator.checkPermit(email);
        paymentService.isValidUpdateTransaction(updateTransaction);
        paymentService.updateTransaction(updateTransaction, email);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/transfer")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update transfer")
    public ResponseEntity<?> updateTransfer(@RequestBody TransferDTO updateTransfer,
                                               @CookieValue(value = "e-mail", defaultValue = "none") String email)
            throws InputIncorrectException {
        accessValidator.checkPermit(email);
        paymentService.isValidUpdateTransfer(updateTransfer);
        paymentService.updateTransfer(updateTransfer, email);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
