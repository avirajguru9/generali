package com.policy.generali.controller;

import com.policy.generali.constant.ApiMessages;
import com.policy.generali.entity.Policy;
import com.policy.generali.service.PolicyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PolicyControllerTest {

    @Mock
    private PolicyService policyService;

    @InjectMocks
    private PolicyController policyController;

    private Policy testPolicy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testPolicy = new Policy();
        testPolicy.setPolicyNumber("POL123");
        testPolicy.setPolicyHolderName("Test Policy");
        // set other required fields
    }

    @Test
    void createPolicy_ShouldReturnCreatedStatus() {
        // Act
        ResponseEntity<?> response = policyController.createPolicy(testPolicy);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(ApiMessages.POLICY_CREATED, response.getBody());
        verify(policyService, times(1)).savePolicy(testPolicy);
    }

    @Test
    void getPolicy_WhenPolicyExists_ShouldReturnPolicy() {
        // Arrange
        when(policyService.getPolicyByNumber("POL123")).thenReturn(Optional.of(testPolicy));

        // Act
        ResponseEntity<Object> response = policyController.getPolicy("POL123");

        // Assert
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(testPolicy, response.getBody());
    }

    @Test
    void getPolicy_WhenPolicyNotExists_ShouldReturnNotFound() {
        // Arrange
        when(policyService.getPolicyByNumber("POL999")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> response = policyController.getPolicy("POL999");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ApiMessages.POLICY_NOT_FOUND, response.getBody());
    }

    @Test
    void updatePolicy_WhenPolicyExists_ShouldReturnOk() {
        // Arrange
        Policy updatedPolicy = new Policy();
        updatedPolicy.setPolicyHolderName("Updated Policy");
        when(policyService.updatePolicy("POL123", updatedPolicy)).thenReturn(Optional.of(updatedPolicy));

        // Act
        ResponseEntity<?> response = policyController.updatePolicy("POL123", updatedPolicy);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ApiMessages.POLICY_UPDATED, response.getBody());
    }

    @Test
    void updatePolicy_WhenPolicyNotExists_ShouldReturnNotFound() {
        // Arrange
        Policy updatedPolicy = new Policy();
        updatedPolicy.setPolicyHolderName("Updated Policy");
        when(policyService.updatePolicy("POL999", updatedPolicy)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = policyController.updatePolicy("POL999", updatedPolicy);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ApiMessages.POLICY_NOT_FOUND, response.getBody());
    }

    @Test
    void deletePolicy_WhenPolicyExists_ShouldReturnOk() {
        // Arrange
        when(policyService.deletePolicy("POL123")).thenReturn(true);

        // Act
        ResponseEntity<?> response = policyController.deletePolicy("POL123");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ApiMessages.POLICY_DELETED, response.getBody());
    }
}