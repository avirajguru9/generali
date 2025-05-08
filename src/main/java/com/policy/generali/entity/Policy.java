package com.policy.generali.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="policy")
public class Policy {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 @NotBlank(message = "Policy number is required")
 @Column(unique = true, nullable = false)
 private String policyNumber;

 @NotBlank(message = "Policy Name is required")
 private String policyHolderName;

 @Min(1)
 private int policyTerm;

 @Min(value = 1, message = "Coverage amount must be positive")
 private double coverageAmount;
 
 @Min(value = 1, message = "Premium amount must be positive")
 private double premium;

 @NotBlank(message = "Policy type is required")
 private String policyType;
}

