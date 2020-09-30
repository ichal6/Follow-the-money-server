package com.mlkb.ftm.controller;

import com.mlkb.ftm.modelDTO.DashboardDTO;
import com.mlkb.ftm.service.DashboardService;
import com.mlkb.ftm.validation.AccessValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;
    private final AccessValidator accessValidator;

    public DashboardController(DashboardService dashboardService, AccessValidator accessValidator) {
        this.dashboardService = dashboardService;
        this.accessValidator = accessValidator;
    }

    @GetMapping("/{email}")
    public ResponseEntity<Object> getDashboard(@PathVariable("email") String email) {
        try{
            this.accessValidator.checkPermit(email);
        } catch (IllegalAccessException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
        }

        try {
            DashboardDTO dashboardDTO = dashboardService.getDashboard(email);
            return new ResponseEntity<>(dashboardDTO, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
