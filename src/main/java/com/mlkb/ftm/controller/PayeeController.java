package com.mlkb.ftm.controller;

import com.mlkb.ftm.modelDTO.PayeeDTO;
import com.mlkb.ftm.service.PayeeService;
import com.mlkb.ftm.service.UserService;
import com.mlkb.ftm.validation.AccessValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/payee")
public class PayeeController {
    private final PayeeService payeeService;
    private final UserService userService;
    private final AccessValidator accessValidator;
    PayeeController(PayeeService payeeService,
                    UserService userService,
                    AccessValidator accessValidator){
        this.payeeService = payeeService;
        this.userService = userService;
        this.accessValidator = accessValidator;
    }

    @GetMapping(value = {"/expense/{email}", "/income/{email}", "/{email}"})
    public ResponseEntity<Set<PayeeDTO>> getPayees(@PathVariable String email) {
        accessValidator.checkPermit(email);
        Set<PayeeDTO> payeesDTO = payeeService.getAllPayees(email);
        return new ResponseEntity<>(payeesDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{email}/{id}")
    public ResponseEntity<Object> deletePayee(@PathVariable String email, @PathVariable Long id) {
        accessValidator.checkPermit(email);
        payeeService.deletePayee(email, id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/{email}")
    public ResponseEntity<Object> createPayee(@PathVariable String email, @RequestBody PayeeDTO payeeDTO){
        accessValidator.checkPermit(email);
        Long userId = userService.getUserId(email);
        payeeService.savePayee(payeeDTO, userId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/{email}/{payeeId}")
    public ResponseEntity<Object> updatePayee(@PathVariable String email, @PathVariable Long payeeId,
                                              @RequestBody String name){
        accessValidator.checkPermit(email);
        payeeService.updatePayee(name, payeeId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
