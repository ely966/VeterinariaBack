package com.example.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoCreasMascotaException  extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7889864048600362145L;

	public NoCreasMascotaException () {
		super("No se ha podido crear la mascota");
	}
}
