package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.error.ApiError;
import com.example.demo.error.CitaExistedException;
import com.example.demo.error.EmailExistedException;
import com.example.demo.error.MascotaNoExistedException;
import com.example.demo.error.UsuarioNoExisteException;
import com.example.demo.model.Cita;
import com.example.demo.model.CreadencialesCitaConId;
import com.example.demo.model.CredencialesEditarUser;
import com.example.demo.model.Mascota;
import com.example.demo.model.User;
import com.example.demo.security.JWTUtil;
import com.example.demo.service.CitaService;
import com.example.demo.service.MascotaService;
import com.example.demo.service.UserService;
import com.example.demo.service.VeterinarioService;
import com.fasterxml.jackson.databind.JsonMappingException;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class VeterinarioController {
	 @Autowired private JWTUtil jwtUtil;
	    @Autowired private AuthenticationManager authManager;
	    @Autowired private PasswordEncoder passwordEncoder;
	    @Autowired private UserService serviUser;
	    @Autowired private VeterinarioService veterinarioServi ;
	    @Autowired private MascotaService mascotaServi;
	    @Autowired private CitaService citaServi;

	    
	    
	    /**
		   * Recoge la informacion del veterinario
		   * @return la informaicon del usuario con rol de veterinario
		   */
		  @GetMapping("/veterinario/info")
		    public User infoVeterinario () {
		    	try {
		    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		            
		            return serviUser.recogerInfoUserPorEmail(email);
		    	}catch (AuthenticationException authExc){
		       	 	throw new UsuarioNoExisteException() ;
		       } 
		    }
		  
		  /**
		     * El veterinario puede editar sus atos
		     * @param veterinario editado
		     * @return
		     */
		    @PutMapping("/veterinario")
		    public User editarVeterinario (@RequestBody CredencialesEditarUser veterinario) {
		    	try {
		    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		            User usuario = serviUser.recogerInfoUserPorEmail(email);
		            return serviUser.edit(veterinario, usuario);
		    	}catch (AuthenticationException authExc){
		       	 	throw new UsuarioNoExisteException() ;
		       } 
		    	
		    }
		    
		    
		    
		    
		    
		    /**
			   * Recoge la lista de citas con este veterinario logeado
			   * @return la lista de citas con este veterinario logeado
			   */
			  @GetMapping("/veterinario/citas")
			    public List<Cita> citasVeterinario () {
			    	try {
			    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			            User veterinario = serviUser.recogerInfoUserPorEmail(email);
			            return veterinarioServi.citasDelVeterinario(veterinario.getId());
			    	}catch (AuthenticationException authExc){
			       	 	throw new UsuarioNoExisteException() ;
			       } 
			    }
			  
			  
			  /**
			     * El veterinario editara la cita para prosponerla y comprobar el estado de la mascota
			     * @param credencialesCitaconId Datos de la cita, id que es el id de la mascota, y el idC que es la id de la cita
			     * @return cita editada
			     */
			    @PutMapping("/veterinario/citas/{idC}")
			    public Cita editarcita(@RequestBody CreadencialesCitaConId cita, @PathVariable Long idC) {
			    	
			    	try{
			    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			       	 	User usuario = serviUser.recogerInfoUserPorEmail(email);
			       	 if (usuario == null) {
			       		 throw new UsuarioNoExisteException();
			 		} else {
			 				if(citaServi.comprobarExistenciaCita(idC)) {
			 					
			 					return citaServi.editarCitaVeterinario(cita, idC);
			 				}else {
			 					throw new CitaExistedException(idC);
			 				}
			 				

			    	}
					}catch (AuthenticationException authExc){
			            throw new UsuarioNoExisteException();
			        }
			        
			    }
			  
			  
			  /**
			   *  public Cita editarMascota(@RequestBody CreadencialesCitaConId cita,@PathVariable Long id, @PathVariable Long idC) {
			    	
			    	try{
			    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			       	 	User usuario = serviUser.recogerInfoUserPorEmail(email);
			       	 if (usuario == null) {
			       		 throw new UsuarioNoExisteException();
			 		} else {

			 			Boolean mascotaExiste = mascotaServi.comprobarporId(id);
			 			if (mascotaExiste) {/**Si existe la cita
			 				
			 				if(citaServi.comprobarExistenciaCita(idC)) {
			 					User cliente = citaServi.recogerClienteDeCita(idC);
			 					return citaServi.editarCita(cita, idC, cliente, id);
			 				}else {
			 					throw new CitaExistedException(idC);
			 				}
			 				
			 			}else {
			 				throw new MascotaNoExistedException();
			 			}
			    	}
					}catch (AuthenticationException authExc){
			            throw new UsuarioNoExisteException();
			        }
			        
			    }
			   */
	/**=================================================================================================**/
		    
	 
		  
		  
		  
		  
		  /**Exceptions**/
		  
		  
		    @ExceptionHandler(EmailExistedException.class)
		   	public ResponseEntity<ApiError> handleEmailNoEncontrado(EmailExistedException  ex) {
		   		ApiError apiError = new ApiError();
		   		apiError.setEstado(HttpStatus.NOT_FOUND);
		   		apiError.setFecha(LocalDateTime.now());
		   		apiError.setMensaje(ex.getMessage());
		   		
		   		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
		   	}
		    
		
		  @ExceptionHandler(UsuarioNoExisteException.class)
			public ResponseEntity<ApiError> handleUserNoEncontrado(UsuarioNoExisteException  ex) {
				ApiError apiError = new ApiError();
				apiError.setEstado(HttpStatus.NOT_FOUND);
				apiError.setFecha(LocalDateTime.now());
				apiError.setMensaje(ex.getMessage());
				
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
			}
		    
			@ExceptionHandler(JsonMappingException.class)
			public ResponseEntity<ApiError> handleJsonMappingException(JsonMappingException ex) {
				ApiError apiError = new ApiError();
				apiError.setEstado(HttpStatus.BAD_REQUEST);
				apiError.setFecha(LocalDateTime.now());
				apiError.setMensaje(ex.getMessage());
				
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
			}  
	    
	    
}
