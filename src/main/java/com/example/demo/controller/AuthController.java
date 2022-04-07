package com.example.demo.controller;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.error.ApiError;
import com.example.demo.error.ComprobarEmailNoexisteException;
import com.example.demo.error.CredencialesInvalidasException;
import com.example.demo.error.EmailExistedException;
import com.example.demo.error.FechaanteriorException;
import com.example.demo.error.NoTokenException;
import com.example.demo.error.UsuarioNoExisteException;
import com.example.demo.model.LoginCredentials;
import com.example.demo.model.Mascota;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;
import com.example.demo.security.JWTUtil;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class AuthController {

    @Autowired private UserRepo userRepo;
    @Autowired private JWTUtil jwtUtil;
    @Autowired private AuthenticationManager authManager;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private UserService serviUser;

    
    
    /**
     * Registra el usuario sí el correo no se repite
     * @param user Objeto de tipo Usuario que sera recibido desde angular y comprende los datos del nuevo usuario
     * @return token . Regresa un token si el correo introducido pore  usuario, no existia en la base de datos. EN caso contrario el token valdra null
     */
    @PostMapping("/auth/register") 
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
	    	   user.setRole("CLIENTE");
	    	   /**Guardamos el usuario con la pass encifrada. Junto su roll, este caso cliente**/
	    	   user= serviUser.agregarUser(user, "CLIENTE");
	    	   return Collections.singletonMap("jwt-token", token);
       }
       else {
    	   throw new EmailExistedException();
       }}catch (AuthenticationException authExc){
       	throw new EmailExistedException() ;
       }
    	
    }
    /**
     * Este metodo recogera los datos del usuario para comprobar is existe y si existe. devuelve un token
     * @param body que seria los datos del usuario. Constara de correo y la contraseña
     * @return regresa el token si existe el usuario y puede logearse. En caso contrario, saltaria un error, pero que no detiene la app
     */

    @PostMapping("/auth/login")
    public Map<String, Object> loginHandler(@RequestBody LoginCredentials body){
    	   /**activar init**/
    
    	try {
    		UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(body.getEmail(), body.getPassword());

            authManager.authenticate(authInputToken);

            String token = jwtUtil.generateToken(body.getEmail());

            return Collections.singletonMap("jwt-token", token);
        }catch (AuthenticationException authExc){
        	throw new CredencialesInvalidasException() ;
        }
    }
    
    
    
    
    @GetMapping(value = "/auth/throwException")
    public void throwException() {
        throw new IllegalArgumentException("\"El correo ya existe\"");
    }
    
   
    
    /**
     * Comprobar en el fronted con formulario reactived
     * @param correo
     * @return
     */
    @GetMapping("/auth/comprobar/{correo}")
    public User comprobarEmail(@PathVariable String correo){
    	   /**activar init**/
    	
    	try {
    		User usuario = userRepo.findByEmail(correo).get();
    		//usuario = userRepo.findByEmail(correo).get();
    		if (usuario == null) {
    			throw new UsuarioNoExisteException();
    		}return usuario;
           
        }catch (AuthenticationException authExc){
        	throw new ComprobarEmailNoexisteException();
        }
    	 
    }
    
    
    @GetMapping("/auth/comprobarToken")
    public User comprobarSiTokenValido(){
    	
    	try {
    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User usuario = serviUser.recogerInfoUserPorEmail(email);
            if(usuario != null) {/**Si el usuario no es null**/
            	return usuario;
            }else {
            	/**No existe el usuario**/
            	 throw new UsuarioNoExisteException() ;
            }
            
    	}catch (AuthenticationException authExc){
            throw new NoTokenException();
            //UsuarioNoExisteException() ;
        }    
 
    }
    
    @GetMapping("/auth/info")
    public User infoUser () {
    	try {
    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            
            return serviUser.recogerInfoUserPorEmail(email);
    	}catch (AuthenticationException authExc){
       	 	throw new UsuarioNoExisteException() ;
       } 
    }
    
    
    /**Control de exception**/
    
    /**
     * Exception personalizada que mostrara el error de manera mas clara
     * @param ex
     * @return
     */
    @ExceptionHandler(CredencialesInvalidasException.class)
	public ResponseEntity<ApiError> handleCredencialNoValido(CredencialesInvalidasException  ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.NOT_FOUND);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}
    
    @ExceptionHandler(EmailExistedException.class)
   	public ResponseEntity<ApiError> handleEmailNoEncontrado(EmailExistedException  ex) {
   		ApiError apiError = new ApiError();
   		apiError.setEstado(HttpStatus.NOT_FOUND);
   		apiError.setFecha(LocalDateTime.now());
   		apiError.setMensaje(ex.getMessage());
   		
   		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
   	}
    
    @ExceptionHandler(FechaanteriorException.class)
   	public ResponseEntity<ApiError> handleFechaAnterior(FechaanteriorException  ex) {
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
