package com.example.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class EmailExistedException extends RuntimeException {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6332728320455029212L;

	public EmailExistedException() {
		super("El email ya existe");
	}

}
