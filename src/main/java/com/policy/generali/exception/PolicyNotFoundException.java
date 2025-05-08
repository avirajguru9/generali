package com.policy.generali.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PolicyNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4900953066245865211L;

	public PolicyNotFoundException(String message) {
        super(message);
    }
}

