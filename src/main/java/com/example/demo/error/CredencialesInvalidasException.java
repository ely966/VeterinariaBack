package com.example.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CredencialesInvalidasException  extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6352181196953653112L;

	public CredencialesInvalidasException() {
		super("Credenciales invalidas");
	}

 
}
