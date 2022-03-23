package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.error.ApiError;
import com.example.demo.error.EmailExistedException;
import com.example.demo.error.UsuarioNoExisteException;
import com.example.demo.model.CredencialesEditarUser;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;
import com.example.demo.security.JWTUtil;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.JsonMappingException;

@CrossOrigin(origins = "http://localhost:4200")
@RestController

public class AdminController {

    @Autowired private JWTUtil jwtUtil;
    @Autowired private AuthenticationManager authManager;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private UserService serviUser;
    @Autowired private UserRepo userRepo;
    
	/**Solo administradores**/

	/**Administradores**/
	
    /**
     *Que un administrador peude registrar otro administrador
     * @param user
     * @return
     */
	  @PostMapping("/admin") 
	    public Map<String, Object> registerHandler(@RequestBody User user){
	        
	    	try {
	    	String encodedPass = passwordEncoder.encode(user.getPassword());
	        
	    	  /**comprobar que el correo que usa este nuevo usuario, no existe en la base de datos **/
	        	String token =null;
	        	Optional<User> usercorreo = userRepo.findByEmail(user.getEmail());
	        	if(usercorreo.isEmpty()) {/**Si el correo no lo tiene ninguna otro usuario**/
		    	   /**comprobar que el username que usa este  usuario, no existe en la base de datos **/
		    	   token = jwtUtil.generateToken(user.getEmail());
		           /**encriptamos la contraseña**/
		    	   user.setPassword(encodedPass);
		    	   /**Añadimos el roll**/
		    	   user.setRole("ADMIN");
		    	   /**Guardamos el usuario con la pass encifrada**/
		    	   user= userRepo.save(user); 
		    	   return Collections.singletonMap("jwt-token", token);
	       }
	       else {
	    	   throw new EmailExistedException();
	       }}catch (AuthenticationException authExc){
	       	throw new EmailExistedException() ;
	       }
	    	
	    }
	  /**
	   * Devuelve los datos de los administradores
	   * @return lista de administardores
	   */
	  
	  @GetMapping("/admin")
	    public User listarAdmin () {
	    	try {
	    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	            
	            return serviUser.recogerInfoUserPorEmail(email);
	    	}catch (AuthenticationException authExc){
	       	 	throw new UsuarioNoExisteException() ;
	       } 
	    }
	  
	  @GetMapping("/admin/info")
	    public User infoAdmin () {
	    	try {
	    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	            
	            return serviUser.recogerInfoUserPorEmail(email);
	    	}catch (AuthenticationException authExc){
	       	 	throw new UsuarioNoExisteException() ;
	       } 
	    }
	  
	  /**
	     * Editar al administradr. Proceso porque salta inacesible
	     * @param adminstrador
	     * @return
	     */
	    @PutMapping("/admin")
	    public User editarAdmin (@RequestBody CredencialesEditarUser admin) {
	    	try {
	    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	            User usuario = userRepo.findByEmail(email).get();
	            return serviUser.edit(admin, usuario);
	    	}catch (AuthenticationException authExc){
	       	 	throw new UsuarioNoExisteException() ;
	       } 
	    	
	    }
	    
	    
	
	/**Veterinario**/
	
	    /**
	     *Que un administrador puede registrar un veterinario
	     * @param user
	     * @return
	     */
		  @PostMapping("/admin/veterinario") 
		    public Map<String, Object> registrarVeterinario(@RequestBody User user){
		        
		    	try {
		    	String encodedPass = passwordEncoder.encode(user.getPassword());
		        
		    	  /**comprobar que el correo que usa este nuevo usuario, no existe en la base de datos **/
		        	String token =null;
		        	Optional<User> usercorreo = userRepo.findByEmail(user.getEmail());
		        	if(usercorreo.isEmpty()) {/**Si el correo no lo tiene ninguna otro usuario**/
			    	   /**comprobar que el username que usa este  usuario, no existe en la base de datos **/
			    	   token = jwtUtil.generateToken(user.getEmail());
			           /**encriptamos la contraseña**/
			    	   user.setPassword(encodedPass);
			    	   /**Añadimos el roll**/
			    	   user.setRole("VETERINARIO");
			    	   /**Guardamos el usuario con la pass encifrada**/
			    	   user= userRepo.save(user); 
			    	   return Collections.singletonMap("jwt-token", token);
		       }
		       else {
		    	   throw new EmailExistedException();
		       }}catch (AuthenticationException authExc){
		       	throw new EmailExistedException() ;
		       }
		    	
		    }

		  
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
