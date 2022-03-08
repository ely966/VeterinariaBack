package com.example.demo.model;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class CredencialesEditarUser {
	private String direccion;
	private String nombre;
	//private String password;
	
	public CredencialesEditarUser() {
		super();
	}

	
}
