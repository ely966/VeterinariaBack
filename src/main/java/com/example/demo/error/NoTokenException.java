package com.example.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class NoTokenException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4224302871686279935L;

	public NoTokenException() {
		super("Token no es valido");
	}

}
