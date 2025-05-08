package com.policy.generali.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.policy.generali.entity.Policy;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, String> {
	Optional<Policy> findByPolicyNumber(String policyNumber);
}

