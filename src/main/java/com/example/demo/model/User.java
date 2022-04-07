package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Entity
//Anotaciones lombok para incluir código de getters, setters, toString y constructor sin argumentos de manera rápida
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	//private String userName;
	private String nombre;
	
	private String email;//Se identifica e user por el correo. El correo que es el usuario apra ingresar a la aplicación.
	private String direccion;
	private int telefono;
	//Evita que el campo password se incluya en el JSON de respuesta
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) /**Que la pass no se incluya en el json respuesta**/
	private String password;
	@OneToMany(mappedBy="usuario")
	@JsonManagedReference("userMascota") 
	private List<Mascota> mascotas; //lista de mascota usado por clientes
	@OneToMany(mappedBy="cliente")
	@JsonManagedReference("userCita")
	private List<Cita>citas; //lista de citas usado sobretodo en clientes
	private String role; //Roll del usuario
	private String tipo;//para veterinario saber el tipo
	

	
	

	public User(String nombre, String email, String password) {
		super();
		//this.userName = userName;
		this.nombre = nombre;
		this.email = email;
		this.password = password;
		
	}
	public User(String nombre, String email, String direccion,int telefono, String password, String role) {
		super();
		//this.userName = userName;
		this.nombre = nombre;
		this.email = email;
		this.direccion=direccion;
		this.telefono=telefono;
		this.password = password;
		this.role=role;
		
	}

	public User(String nombre, String email, String direccion,int telefono, String password, String role, String tipo) {
		super();
		//this.userName = userName;
		this.nombre = nombre;
		this.email = email;
		this.direccion=direccion;
		this.telefono=telefono;
		this.password = password;
		this.role=role;
		this.tipo= tipo;
		
	}
	
	public User(String nombre, String email, String password, String direccion) {
		super();
		//this.userName = userName;
		this.direccion = direccion;
		this.nombre = nombre;
		this.email = email;
		this.password = password;
		
	}






	
	


	
	
}
