package com.policy.generali.controller;

import com.policy.generali.constant.ApiMessages;
import com.policy.generali.entity.Policy;
import com.policy.generali.service.PolicyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    @Autowired
    private PolicyService service;

    @PostMapping("/create")
    public ResponseEntity<?> createPolicy(@Valid @RequestBody Policy policy) {
        service.savePolicy(policy);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiMessages.POLICY_CREATED);
    }

    @GetMapping("/{policyNumber}")
    public ResponseEntity<Object> getPolicy(@PathVariable("policyNumber") String policyNumber) {
        return service.getPolicyByNumber(policyNumber)
                .<ResponseEntity<Object>>map(policy -> ResponseEntity.status(HttpStatus.FOUND).body(policy))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiMessages.POLICY_NOT_FOUND));
    }
    
    @PutMapping("/update/{policyNumber}")
    public ResponseEntity<?> updatePolicy(@PathVariable("policyNumber") String policyNumber,
                                          @Valid @RequestBody Policy updatedPolicy) {
        return service.updatePolicy(policyNumber, updatedPolicy)
                .<ResponseEntity<?>>map(policy -> ResponseEntity.ok(ApiMessages.POLICY_UPDATED))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiMessages.POLICY_NOT_FOUND));
    }

    @DeleteMapping("/{policyNumber}")
    public ResponseEntity<?> deletePolicy(@PathVariable("policyNumber") String policyNumber) {
        boolean deleted = service.deletePolicy(policyNumber);
        if (deleted) {
            return ResponseEntity.ok(ApiMessages.POLICY_DELETED);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiMessages.POLICY_DELETED);
        }
    }

}