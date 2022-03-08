package com.example.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UsuarioNoExisteException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1612069456974806574L;

	public UsuarioNoExisteException () {
		super("No existe el usuario o no es valido");
	}

}
