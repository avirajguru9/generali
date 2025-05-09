package com.policy.generali.controller;

import com.policy.generali.constant.ApiMessages;
import com.policy.generali.dto.ApiResponse;
import com.policy.generali.entity.Policy;
import com.policy.generali.service.PolicyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    @Autowired
    private PolicyService service;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Policy>> createPolicy(@Valid @RequestBody Policy policy) {
        try {
            Policy createdPolicy = service.savePolicy(policy);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, ApiMessages.POLICY_CREATED, createdPolicy, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, ApiMessages.CREATE_POLICY_ERROR, null, "API_500"));
        }
    }

    @GetMapping("/{policyNumber}")
    public ResponseEntity<ApiResponse<Policy>> getPolicy(@PathVariable String policyNumber) {
        try {
            return service.getPolicyByNumber(policyNumber)
                    .map(policy -> ResponseEntity.ok(
                            new ApiResponse<>(true, ApiMessages.POLICY_FOUND, policy, null)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ApiResponse<>(false, ApiMessages.POLICY_NOT_FOUND, null, "API_404")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, ApiMessages.GET_POLICY_ERROR, null, "API_500"));
        }
    }
    
    @PutMapping("/update/{policyNumber}")
    public ResponseEntity<ApiResponse<Policy>> updatePolicy(
            @PathVariable String policyNumber,
            @Valid @RequestBody Policy updatedPolicy) {
        try {
            return service.updatePolicy(policyNumber, updatedPolicy)
                    .map(policy -> ResponseEntity.ok(
                            new ApiResponse<>(true, ApiMessages.POLICY_UPDATED, policy, null)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ApiResponse<>(false, ApiMessages.POLICY_NOT_FOUND, null, "API_404")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, ApiMessages.UPDATE_POLICY_ERROR, null, "API_500"));
        }
    }

    @DeleteMapping("/{policyNumber}")
    public ResponseEntity<ApiResponse<Void>> deletePolicy(@PathVariable String policyNumber) {
        try {
            boolean deleted = service.deletePolicy(policyNumber);
            if (deleted) {
                return ResponseEntity.ok(
                        new ApiResponse<Void>(true, ApiMessages.POLICY_DELETED, null, null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<Void>(false, ApiMessages.POLICY_NOT_FOUND, null, "API_404"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<Void>(false, ApiMessages.DELETE_POLICY_ERROR, null, "API_500"));
        }
    }
}