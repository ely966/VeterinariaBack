
package com.example.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)

public class CitaYaExisteException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2088811966883328334L;

	public CitaYaExisteException() {
		super("Ya existe esa cita con esa fecha y hora");
	}
}
