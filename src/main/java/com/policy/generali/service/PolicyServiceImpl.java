package com.policy.generali.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.policy.generali.entity.Policy;
import com.policy.generali.repository.PolicyRepository;

@Service
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository repository;

    public PolicyServiceImpl(PolicyRepository repository) {
        this.repository = repository;
    }

    @Override
    public Policy savePolicy(Policy policy) {
        return repository.save(policy);
    }
    
    @Override
    public Optional<Policy> updatePolicy(String policyNumber, Policy updatedPolicy) {
        return repository.findByPolicyNumber(policyNumber).map(existingPolicy -> {
            existingPolicy.setPolicyHolderName(updatedPolicy.getPolicyHolderName());
            existingPolicy.setPolicyTerm(updatedPolicy.getPolicyTerm());
            existingPolicy.setCoverageAmount(updatedPolicy.getCoverageAmount());
            existingPolicy.setPolicyType(updatedPolicy.getPolicyType());
            existingPolicy.setPremium(updatedPolicy.getPremium());
            return repository.save(existingPolicy);
        });
    }
    
    @Override
    public boolean deletePolicy(String policyNumber) {
        Optional<Policy> policy = repository.findByPolicyNumber(policyNumber);
        if (policy.isPresent()) {
            repository.delete(policy.get());
            return true;
        }
        return false;
    }



    @Override
    public Optional<Policy> getPolicyByNumber(String policyNumber) {
        return repository.findByPolicyNumber(policyNumber);
    }
}

