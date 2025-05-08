package com.policy.generali.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import com.policy.generali.entity.Policy;
import com.policy.generali.repository.PolicyRepository;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class PolicyServiceImplTest {

    @Mock
    private PolicyRepository repository;

    @InjectMocks
    private PolicyServiceImpl service;;

    private Policy policy;

    @BeforeEach
    void setUp() {
    	System.out.println("In test case");
        policy = new Policy(15L, "P123", "John Doe", 10000, 5, 500, "Health");
    }

    @Test
    void testGetPolicyByNumber_WhenExists() {
        when(repository.findByPolicyNumber("P123")).thenReturn(Optional.of(policy));

        Optional<Policy> result = service.getPolicyByNumber("P123");

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getPolicyHolderName());
    }

    @Test
    void testGetPolicyByNumber_WhenNotExists() {
        when(repository.findByPolicyNumber("P999")).thenReturn(Optional.empty());

        Optional<Policy> result = service.getPolicyByNumber("P999");

        assertFalse(result.isPresent());
    }

    @Test
    void testCreatePolicy() {
        when(repository.save(policy)).thenReturn(policy);

        Policy saved = service.savePolicy(policy);

        assertNotNull(saved);
        assertEquals("P123", saved.getPolicyNumber());
        verify(repository, times(1)).save(policy);
    }

    @Test
    void testDeletePolicy_WhenExists() {
        when(repository.findByPolicyNumber("P123")).thenReturn(Optional.of(policy));

        boolean deleted = service.deletePolicy("P123");

        assertTrue(deleted);
        verify(repository, times(1)).delete(policy);
    }

    @Test
    void testDeletePolicy_WhenNotExists() {
        when(repository.findByPolicyNumber("P999")).thenReturn(Optional.empty());

        boolean deleted = service.deletePolicy("P999");

        assertFalse(deleted);
        verify(repository, never()).delete(any());
    }
}


