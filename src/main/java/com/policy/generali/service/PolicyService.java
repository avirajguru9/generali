package com.policy.generali.service;

import java.util.Optional;

import com.policy.generali.entity.Policy;

public interface PolicyService {
    Policy savePolicy(Policy policy);
    Optional<Policy> getPolicyByNumber(String policyNumber);
    Optional<Policy> updatePolicy(String policyNumber, Policy updatedPolicy);
    boolean deletePolicy(String policyNumber);
}


