package com.example.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)

public class CitaExistedException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 959607598248748076L;

	public CitaExistedException(Long id) {
		super("No existe esa cita");
	}
}
