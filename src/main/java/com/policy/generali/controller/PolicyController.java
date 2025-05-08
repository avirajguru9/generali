package com.policy.generali.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.policy.generali.entity.Policy;
import com.policy.generali.service.PolicyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    @Autowired
    private PolicyService service;

    @PostMapping("/create")
    public ResponseEntity<?> createPolicy(@Valid @RequestBody Policy policy) {
        service.savePolicy(policy);
        return ResponseEntity.status(201).body("Policy Created successfully");
    }

    @GetMapping("/{policyNumber}")
    public ResponseEntity<Object> getPolicy(@PathVariable("policyNumber") String policyNumber) {
        return service.getPolicyByNumber(policyNumber)
                .<ResponseEntity<Object>>map(policy -> ResponseEntity.status(201).body(policy))
                .orElse(ResponseEntity.status(404).body("Policy Does not exist"));
    }
    
    @PutMapping("/update/{policyNumber}")
    public ResponseEntity<?> updatePolicy(@PathVariable("policyNumber") String policyNumber,
                                          @Valid @RequestBody Policy updatedPolicy) {
        return service.updatePolicy(policyNumber, updatedPolicy)
                .<ResponseEntity<?>>map(policy -> ResponseEntity.ok("Policy updated successfully"))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Policy does not exist"));
    }

    @DeleteMapping("/{policyNumber}")
    public ResponseEntity<?> deletePolicy(@PathVariable("policyNumber") String policyNumber) {
        boolean deleted = service.deletePolicy(policyNumber);
        if (deleted) {
            return ResponseEntity.ok("Policy deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Policy does not exist");
        }
    }

}

