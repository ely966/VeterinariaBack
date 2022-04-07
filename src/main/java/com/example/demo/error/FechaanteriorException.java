package com.example.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FechaanteriorException extends RuntimeException{

	

	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4124559118256687198L;

	public FechaanteriorException() {
		super("La fecha introducida no es valida porque es anterior a la fecha de hoy");
	}
}
