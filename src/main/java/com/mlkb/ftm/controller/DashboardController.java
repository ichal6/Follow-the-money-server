package com.mlkb.ftm.controller;

import com.mlkb.ftm.modelDTO.DashboardDTO;
import com.mlkb.ftm.service.DashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService){
        this.dashboardService = dashboardService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<Object> getDashboard(@PathVariable("email") String email){
        Optional<DashboardDTO> optionalDashboardDTO = dashboardService.getDashboard(email);
        if (optionalDashboardDTO.isPresent()) {
            return new ResponseEntity<>(optionalDashboardDTO.get(), HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().body("The user with this id does not exists in the database!");
        }
    }
}
