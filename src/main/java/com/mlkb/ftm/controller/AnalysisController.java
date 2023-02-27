package com.mlkb.ftm.controller;

import com.mlkb.ftm.modelDTO.AnalysisFinancialTableDTO;
import com.mlkb.ftm.service.AnalysisService;
import com.mlkb.ftm.validation.AccessValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {
    private final AnalysisService analysisService;
    private final AccessValidator accessValidator;


    public AnalysisController(AnalysisService analysisService, AccessValidator accessValidator) {
        this.analysisService = analysisService;
        this.accessValidator = accessValidator;
    }

    @GetMapping("/{email}")
    public ResponseEntity<Object> getAnalysisTableForUser(@PathVariable("email") String email,
                                                          @RequestParam("start") Optional<String> possibleStart) {
        this.accessValidator.checkPermit(email);

        Instant startDate = this.analysisService.convertParamToInstant(possibleStart);
        Set<AnalysisFinancialTableDTO> analysisFinancialTableDTOSet = analysisService.getTableData(email, startDate);
        return new ResponseEntity<>(analysisFinancialTableDTOSet, HttpStatus.OK);
    }
}
