package com.example.demo.repository;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Cita;


@Repository
public interface CitasRepository extends JpaRepository<Cita, Long>{
	/**Citas for fecha**/
	public Optional<List<Cita>> findByFecha(Date date);
	public Optional<Cita> findByHora(java.util.Date hora);
	public Optional<Cita> findByFechaCompleta(Date fecha);
	
	/**CItas por veterinario**/
	//public Optional<List<Cita>> findByFecha(Date date);
	public Optional<List<Cita>> findByIdVeterinario(Long idVeterinario);
}
