package com.example.demo.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Mascota {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String nombre;
	private String tipo;
	private String raza;
	private int edad;
	private String foto;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JsonBackReference("userMascota")
	private User usuario;
	
	public Mascota(String nombre, String tipo, String raza, int edad, User usuario) {
		super();
		this.nombre = nombre;
		this.tipo = tipo;
		this.raza = raza;
		this.edad = edad;
		this.usuario = usuario;
	}
	public Mascota(String nombre, String tipo, String raza, int edad) {
		super();
		this.nombre = nombre;
		this.tipo = tipo;
		this.raza = raza;
		this.edad = edad;
		
	}
	
	
}
