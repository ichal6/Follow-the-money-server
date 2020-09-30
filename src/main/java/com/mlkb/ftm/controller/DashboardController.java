package com.mlkb.ftm.controller;

import com.mlkb.ftm.modelDTO.DashboardDTO;
import com.mlkb.ftm.service.DashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<Object> getDashboard(@PathVariable("email") String email) {
        if (!checkPermit(email)) {
            return new ResponseEntity<>("Access Denied", HttpStatus.FORBIDDEN);
        }
        try {
            DashboardDTO dashboardDTO = dashboardService.getDashboard(email);
            return new ResponseEntity<>(dashboardDTO, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private boolean checkPermit(String email) {
        String emailFromToken = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(emailFromToken == null){
            return false;
        }
        return email.equals(emailFromToken);
    }
}
