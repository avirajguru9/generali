package com.policy.generali.controller;

import com.policy.generali.constant.ApiMessages;
import com.policy.generali.dto.ApiResponse;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
        testPolicy.setPolicyType("Life Insurance");
        testPolicy.setPremium(1000.0);
    }

    // CREATE POLICY TESTS
    @Test
    void createPolicy_ShouldReturnCreatedResponse_WhenValidPolicy() {
        // Arrange
        when(policyService.savePolicy(any(Policy.class))).thenReturn(testPolicy);
        
        // Act
        ResponseEntity<ApiResponse<Policy>> response = policyController.createPolicy(testPolicy);
        
        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals(ApiMessages.POLICY_CREATED, response.getBody().getMessage());
        assertEquals(testPolicy, response.getBody().getData());
        verify(policyService, times(1)).savePolicy(testPolicy);
    }

    // GET POLICY TESTS
    @Test
    void getPolicy_ShouldReturnPolicy_WhenPolicyExists() {
        // Arrange
        when(policyService.getPolicyByNumber("POL123")).thenReturn(Optional.of(testPolicy));
        
        // Act
        ResponseEntity<ApiResponse<Policy>> response = policyController.getPolicy("POL123");
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals(ApiMessages.POLICY_FOUND, response.getBody().getMessage());
        assertEquals(testPolicy, response.getBody().getData());
    }

    @Test
    void getPolicy_ShouldReturnNotFound_WhenPolicyDoesNotExist() {
        // Arrange
        when(policyService.getPolicyByNumber("NONEXISTENT")).thenReturn(Optional.empty());
        
        // Act
        ResponseEntity<ApiResponse<Policy>> response = policyController.getPolicy("NONEXISTENT");
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals(ApiMessages.POLICY_NOT_FOUND, response.getBody().getMessage());
        assertNull(response.getBody().getData());
        assertEquals("API_404", response.getBody().getErrorCode());
    }

    // UPDATE POLICY TESTS
    @Test
    void updatePolicy_ShouldReturnOk_WhenPolicyExists() {
        // Arrange
        when(policyService.updatePolicy("POL123", testPolicy)).thenReturn(Optional.of(testPolicy));
        
        // Act
        ResponseEntity<ApiResponse<Policy>> response = 
            policyController.updatePolicy("POL123", testPolicy);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals(ApiMessages.POLICY_UPDATED, response.getBody().getMessage());
        assertEquals(testPolicy, response.getBody().getData());
    }

    @Test
    void updatePolicy_ShouldReturnNotFound_WhenPolicyDoesNotExist() {
        // Arrange
        when(policyService.updatePolicy("NONEXISTENT", testPolicy)).thenReturn(Optional.empty());
        
        // Act
        ResponseEntity<ApiResponse<Policy>> response = 
            policyController.updatePolicy("NONEXISTENT", testPolicy);
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals(ApiMessages.POLICY_NOT_FOUND, response.getBody().getMessage());
        assertNull(response.getBody().getData());
        assertEquals("API_404", response.getBody().getErrorCode());
    }

    // DELETE POLICY TESTS
    @Test
    void deletePolicy_ShouldReturnOk_WhenPolicyExists() {
        // Arrange
        when(policyService.deletePolicy("POL123")).thenReturn(true);
        
        // Act
        ResponseEntity<ApiResponse<Void>> response = policyController.deletePolicy("POL123");
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals(ApiMessages.POLICY_DELETED, response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void deletePolicy_ShouldReturnNotFound_WhenPolicyDoesNotExist() {
        // Arrange
        when(policyService.deletePolicy("NONEXISTENT")).thenReturn(false);
        
        // Act
        ResponseEntity<ApiResponse<Void>> response = policyController.deletePolicy("NONEXISTENT");
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals(ApiMessages.POLICY_NOT_FOUND, response.getBody().getMessage());
        assertNull(response.getBody().getData());
        assertEquals("API_404", response.getBody().getErrorCode());
    }

    // EXCEPTION HANDLING TESTS
    @Test
    void createPolicy_ShouldHandleServiceException() {
        // Arrange
        doThrow(new RuntimeException("Database error")).when(policyService).savePolicy(any());
        
        // Act
        ResponseEntity<ApiResponse<Policy>> response = policyController.createPolicy(testPolicy);
        
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals(ApiMessages.CREATE_POLICY_ERROR, response.getBody().getMessage());
        assertEquals("API_500", response.getBody().getErrorCode());
    }

    @Test
    void getPolicy_ShouldHandleServiceException() {
        // Arrange
        when(policyService.getPolicyByNumber("POL123")).thenThrow(new RuntimeException("DB error"));
        
        // Act
        ResponseEntity<ApiResponse<Policy>> response = policyController.getPolicy("POL123");
        
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals(ApiMessages.GET_POLICY_ERROR, response.getBody().getMessage());
        assertEquals("API_500", response.getBody().getErrorCode());
    }
}