package com.example.demo.model;



import java.util.Date;



import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor


public class CredencialesCita {
	private Date fecha;
	private Long petid;
	private String motivo;
	private Long idVeterinario;
	
	public CredencialesCita() {
		super();
	}
	
	
}
