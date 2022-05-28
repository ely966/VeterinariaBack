package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.example.demo.service.AdminService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.JsonMappingException;

@CrossOrigin(origins = "http://localhost:4200")
@RestController

public class AdminController {

    @Autowired private JWTUtil jwtUtil;
    @Autowired private AuthenticationManager authManager;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private UserService serviUser;
    @Autowired private AdminService serviAdmin;
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
	    	/**comprobar que el correo que usa este nuevo usuario, no existe en la base de datos **/
		    	String token =null;
		        Optional<User> usercorreo = userRepo.findByEmail(user.getEmail());
		        if(usercorreo.isEmpty()) {/**Si el correo no lo tiene ninguna otro usuario**/
		    	   //**comprobar que el username que usa este  usuario, no existe en la base de datos **/
		    	   token = jwtUtil.generateToken(user.getEmail());
		    	   //**Llamamos almetodo del servicio de adminServi el cual recogerá el usuario y su role
		    	   //añadira la contraseña encriptada, su roll  y lo guardará en el repositorio//
		    	   serviUser.agregarUser(user, "ADMIN");
		    	   return Collections.singletonMap("jwt-token", token);
		    	   }
		        else {
		        	throw new EmailExistedException();
		        	}
		    }catch (AuthenticationException authExc){
	       	throw new EmailExistedException() ;
	       }
	    	
	    }
	  /**
	   * Devuelve los datos de los administradores
	   * @return lista de administardores
	   */
	  
	  @GetMapping("/admin")
	    public List<User> listarAdmin () {
	    	try {
	    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	            
	            return serviAdmin.findAllAdmin();
	    	}catch (AuthenticationException authExc){
	       	 	throw new UsuarioNoExisteException() ;
	       } 
	    }
	  /**
	   * Recoge la informacion del administrador
	   * @return la informaicon del usuario con rol de administrador
	   */
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
	            User usuario = serviUser.recogerInfoUserPorEmail(email);
	            return serviUser.edit(admin, usuario);
	    	}catch (AuthenticationException authExc){
	       	 	throw new UsuarioNoExisteException() ;
	       } 
	    	
	    }
	    //*Recoger valor de un usuario*/
	    
	    @GetMapping("/admin/cliente/{correo}")
	    public User infoCliente (@PathVariable String correo) {
	    	try {
	            return serviUser.recogerInfoUserPorEmail(correo);
	    	}catch (AuthenticationException authExc){
	       	 	throw new UsuarioNoExisteException() ;
	       } 
	    }
	    
	    /**
	     *Que un administrador puede cmabair al contraseña de cualquier otro usuario
	     * @param datosUser que es la contarseña nueva, user que es el usuario que cambaira la pass
	     * @return
	     */
		  @PutMapping("/admin/user/{id}") 
		    public User cambiarPasswordUser(@RequestBody CredencialesEditarUser datosUser, @PathVariable Long id ){
		        
		    	try {
		    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		            //Comprobar que existe el usuario del token del administrador
		    		User usuario = serviUser.recogerInfoUserPorEmail(email);
			        /**Comprueba que el usuario a editar existe**/
			    	User usercorreo = serviUser.findById(id);
			        if(usercorreo != null) {/**Si el usuario existe**/
			    	  
			    	   return serviUser.edit(datosUser, usercorreo);
			    	   }
			        else {
			        	throw new UsuarioNoExisteException();
			        	}
			    }catch (AuthenticationException authExc){
		       	throw new UsuarioNoExisteException() ;
		       }
		    	
		    }
/**=================================================================================================**/
	    
 
	    
/*=======================================================================================*/	    
	
	/**Admin Funciones sobre Veterinario**/
	
	    /**
	     *Que un administrador puede registrar un veterinario
	     * @param user
	     * @return
	     */
		  @PostMapping("/admin/veterinario") 
		    public Map<String, Object> registrarVeterinario(@RequestBody User user){
		        
		    	try {
		    		/**comprobar que el correo que usa este nuevo usuario, no existe en la base de datos **/
		        	String token =null;
				    Optional<User> usercorreo = userRepo.findByEmail(user.getEmail());
				    if(usercorreo.isEmpty()) {/**Si el correo no lo tiene ninguna otro usuario**/
				    	//**comprobar que el username que usa este  usuario, no existe en la base de datos **/
				    	token = jwtUtil.generateToken(user.getEmail());
				    	//**Llamamos almetodo del servicio de adminServi el cual recogerá el usuario y su role
				    	//añadira la contraseña encriptada, su roll  y lo guardará en el repositorio//
				    	serviUser.agregarUser(user, "VETERINARIO");
				    	return Collections.singletonMap("jwt-token", token);
		       }
		       else {
		    	   throw new EmailExistedException();
		       }}catch (AuthenticationException authExc){
		       	throw new EmailExistedException() ;
		       }
		    	
		    }
		  
		  /**
		   * Devuelve la lista de usuario con el rol de veterinario
		   * @return lista de veterinario
		   */
		  @GetMapping("/admin/veterinario")
		    public List<User> listarVeterinarios () {
		    	try {
		    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		            
		            return serviAdmin.findAllVeterinarios();
		    	}catch (AuthenticationException authExc){
		       	 	throw new UsuarioNoExisteException() ;
		       } 
		    }
		  
		  /**
		     *Que un administrador puede borrar un veterinario
		     * @param user
		     * @return
		     */
			  @DeleteMapping("/admin/veterinario/{id}") 
			    public  ResponseEntity<?> deleteVeterinario(@PathVariable Long id){
			        
			    	try {

			    		/**comprobar que el correo que usa este admin,  existe en la base de datos **/
			    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			            User usuario = serviUser.recogerInfoUserPorEmail(email);
			            if (usuario !=null) {//si el admin existe
			            	User veterinarioABorrar = serviUser.findById(id);//recogemos el usuario veterinario que deseamos borrar
			            	serviAdmin.deleteVeterinario(veterinarioABorrar);
			            	return ResponseEntity.noContent().build();	
			            }else {
			            		throw new UsuarioNoExisteException();
			           }
			      
			       }catch (AuthenticationException authExc){
			       	throw new UsuarioNoExisteException() ;
			       }
			    	
			    }

			  /**
			     *Que un administrador puede borrar un admin
			     * @param user
			     * @return
			     */
				  @DeleteMapping("/admin/{id}") 
				    public  ResponseEntity<?> deleteAdmin(@PathVariable Long id){
				        
				    	try {

				    		/**comprobar que el correo que usa este admin,  existe en la base de datos **/
				    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				            User usuario = serviUser.recogerInfoUserPorEmail(email);
				            if (usuario !=null) {//si el admin existe
				            	
				            	serviUser.delete(id);
				            	
				            	return ResponseEntity.noContent().build();	
				            }else {
				            		throw new UsuarioNoExisteException();
				           }
				      
				       }catch (AuthenticationException authExc){
				       	throw new UsuarioNoExisteException() ;
				       }
				    	
				    }
	  
		  /**
		   * Devuelve la lista de usuario con el rol de ciente
		   * @return lista de clientes
		   */
		  @GetMapping("/admin/clientes")
		    public List<User> listatodosusuarioClientes () {
		    	try {
		    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		            
		            return serviAdmin.findAllCliente();
		    	}catch (AuthenticationException authExc){
		       	 	throw new UsuarioNoExisteException() ;
		       } 
		    }
		  
/*==========*/
		  /**
		     *Que un administrador puede borrar un cliente
		     * @param user
		     * @return
		     */
			  @DeleteMapping("/admin/cliente/{id}") 
			    public  ResponseEntity<?> deleteCliente(@PathVariable Long id){
			        
			    	try {

			    		/**comprobar que el correo que usa este admin,  existe en la base de datos **/
			    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			            User usuario = serviUser.recogerInfoUserPorEmail(email);
			            if (usuario !=null) {//si el admin existe
			            	User clienteABorrar = serviUser.findById(id);//recogemos el usuario veterinario que deseamos borrar
			            	serviUser.delete(clienteABorrar.getId());
			            	return ResponseEntity.noContent().build();	
			            }else {
			            		throw new UsuarioNoExisteException();
			           }
			      
			       }catch (AuthenticationException authExc){
			       	throw new UsuarioNoExisteException() ;
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
