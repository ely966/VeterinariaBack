package com.example.demo.service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.error.ApiError;

import com.example.demo.error.CitaYaExisteException;
import com.example.demo.error.UsuarioNoExisteException;
import com.example.demo.model.Cita;
import com.example.demo.model.CreadencialesCitaConId;
import com.example.demo.model.CredencialesCita;
import com.example.demo.model.Mascota;
import com.example.demo.model.User;
import com.example.demo.repository.CitasRepository;
import com.example.demo.repository.MascotaRepository;


@Primary
@Service("CitaService")
public class CitaService {
	@Autowired CitasRepository citaRepo;
	 @Autowired private MascotaService mascotaServi;
	 @Autowired private MascotaRepository mascotaRepo;
	 
	 
	 /**
	  * Añade una nueva cita. Comprueba que la fecha y hora no coincida con otra cita ya existente en el banner
	  * @param cita
	  * @param usuario,cita
	  * @return cita nueva, pero si ya esta recogida una cita con a fecha seleccionada, volvera una cita vacia, para que salte la exception en el controlador
	  */
	public Cita addCita(CredencialesCita cita,User usuario, Long pet) {
		Cita nuevaCita = new Cita();

			/**Recogeremos aqui las citas que tenga la misma fecha**/
			Cita citaConFechaCompleta=null;
			citaConFechaCompleta=citaRepo.findByFechaCompleta(cita.getFecha()).orElse(null);
			if (citaConFechaCompleta ==null) {//Si esa fecha esta disponible
				/**Añadimos cada datos de la cita**/
				nuevaCita.setCliente(usuario);
				/**Debemos ir comprobando la fecha**/
				nuevaCita.setFecha(cita.getFecha());
				nuevaCita.setHora(cita.getFecha());
				nuevaCita.setFechaCompleta(cita.getFecha());
				nuevaCita.setPet(mascotaRepo.findById(pet).get());
				nuevaCita.setIdVeterinario(cita.getIdVeterinario());
				nuevaCita.setMotivo(cita.getMotivo());
				//añadimos el numero del cliente
				nuevaCita.setNumeroContacto(usuario.getTelefono());
				/**Guardamos la cita**/
				citaRepo.save(nuevaCita);
				
				
			}
				return nuevaCita;
			
	}
	
	

	
	/**
	 * Encontrar una cita por su id
	 * @param id
	 * @return cita encontrada
	 */
	public Cita findCita(Long id){
		Cita cita = new Cita();
		cita = citaRepo.findById(id).get();
		return cita;
	}
	
	
	/**
	 * Mostrar la lista de cita d eun cliente
	 * @param cliente
	 * @return lista de cita de un cliente
	 */
	public List<Cita> mostrarCitasUser(User cliente){
		return cliente.getCitas();
		
	}
	/**
	 * Eliminar una cita
	 * @param usuario
	 * @param cita
	 * @return 
	 */
	public Cita delete(User usuario,Cita cita) {
		
		/**Eliminamos su union con la mascota**/
		cita.setPet(null);
		//**Eliminamos su union con cliente**/
		cita.setCliente(null);
		cita.setIdVeterinario(null);
		//Borramos la fecha para que no salte la contradiccion de que, es una fecha del pasado si llega el caso
		cita.setFecha(null);
		//borramos el numero
		cita.setNumeroContacto(0);
		//**Guardamos la cita sin uniones**/
		citaRepo.save(cita);
		citaRepo.delete(cita);/**Eliminamos la cita**/
		return cita;
	}
	
	/**
	 * Borrar las citas de una mascota
	 * @param id de pet
	 */
	
	public void deleteByPet(Long id) {
		List<Cita>citas=citaRepo.findAll();
		for (int i=0; citaRepo.count() > i;i=i+1) {
			if(citas.get(i).getPet().getId() == id) {
				citas.get(i).setPet(null);
				citas.get(i).setCliente(null);
				citaRepo.save(citas.get(i));
				citaRepo.deleteById(citas.get(i).getId());
			}
		}
	}
	
	/**
	 * Comprobar si existe la cita
	 * @param id
	 * @return boolean
	 */
	public Boolean comprobarExistenciaCita(Long id) {
		return citaRepo.existsById(id);
	}
	
	/**
	 * Recoge una cita por su id
	 * @param id
	 * @return
	 */
	public Cita encontrarCitaId (Long id) {
		return citaRepo.findById(id).get();
	}
	
	/**
	 * Edtar una cita
	 * @param cita
	 * @param usuario
	 * @param id
	 * @return cita editada
	 */
	
	public Cita editarCita(CreadencialesCitaConId cita,Long idCita, User usuario, Long id) {
		Cita citaEditada = new Cita();
		citaEditada.setId(idCita);
		citaEditada.setCliente(usuario);
		//comprobamos si edito la fecha. Si esta vacio no se edita
		if(cita.getFecha() == null) {
			citaEditada.setFecha(cita.getFecha());
			citaEditada.setHora(cita.getFecha());
			citaEditada.setFechaCompleta(cita.getFecha());
		}
		
		//comprobamos que el numero de contacto esta vacio o no
		if(cita.getNumeroContacto() > 0) {
			citaEditada.setNumeroContacto(cita.getNumeroContacto());
		}
		
		
		Mascota mascota= mascotaServi.encontrarId(cita.getPetid());
		citaEditada.setPet(mascotaServi.encontrarId(cita.getPetid()));
		citaEditada.setMotivo(cita.getMotivo());
		citaRepo.save(citaEditada);
		return citaEditada;
	}
	
	
	public List<Cita> mostrarPorPet(Long idM){
		List<Cita> citasAll = citaRepo.findAll();
		List<Cita> citas = new ArrayList();
		
		for(int i =0; citaRepo.count() > i; i=i+1) {
			if (citasAll.get(i).getPet().getId() == idM) {
				citas.add(citasAll.get(i));
			}
		}
		return citas;
	}
	
	public List<Cita> mostrarPorVeterinario(Long idV){
		List<Cita> citasAll = citaRepo.findAll();
		List<Cita> citas = new ArrayList();
		citas= citaRepo.findByIdVeterinario(idV).get();
		return citas;
	}

	
	
    //*Exception cuando no ha podido crear la mascota**//            @Override

    @ExceptionHandler(CitaYaExisteException.class)
	public ResponseEntity<ApiError> handlecitaYaExisteException(CitaYaExisteException  ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.NOT_FOUND);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}
    

    
}
