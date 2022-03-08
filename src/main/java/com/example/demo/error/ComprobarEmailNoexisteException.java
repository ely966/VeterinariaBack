package com.example.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ComprobarEmailNoexisteException extends RuntimeException {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6657535604244991712L;

	public ComprobarEmailNoexisteException() {
		super("El email no existe, puedes crearlo");
	}

}
