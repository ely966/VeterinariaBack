package com.example.demo.model;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Cita {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonBackReference("userCita")  
	@JoinColumn(name="Cliente")
	private User cliente;
	@ManyToOne(fetch = FetchType.EAGER)
	private Mascota pet;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd ", timezone = "GMT+8")
	private Date fecha;
	@Temporal(TemporalType.TIME)
	private Date hora;
	private String motivo;


	
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaCompleta;
	
	
	public Cita(Mascota pet, Date fecha, Time hora, String motivo) {
		super();
		this.pet = pet;
		this.fecha = fecha;
		this.hora = hora;
		this.motivo = motivo;
	}


	public Cita(Mascota pet, Date fecha, String motivo) {
		super();
		this.pet = pet;
		this.fecha = fecha;
		this.hora=hora;
		this.motivo = motivo;
	}
	
	
	
	
}
