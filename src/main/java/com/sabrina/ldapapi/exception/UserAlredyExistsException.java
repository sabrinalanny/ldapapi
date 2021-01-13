package com.sabrina.ldapapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Sabrina
 *
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlredyExistsException extends RuntimeException {

	private static final long serialVersionUID = -2533194229906054487L;

	public UserAlredyExistsException(String message) {
		super(message);
	}
}