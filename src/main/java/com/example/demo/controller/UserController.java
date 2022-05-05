package com.example.demo.controller;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.error.ApiError;
import com.example.demo.error.CitaExistedException;
import com.example.demo.error.CitaYaExisteException;
import com.example.demo.error.CredencialesInvalidasException;
import com.example.demo.error.EmailExistedException;
import com.example.demo.error.FechaanteriorException;
import com.example.demo.error.MascotaExistedException;
import com.example.demo.error.MascotaNoExistedException;
import com.example.demo.error.NoCreasMascotaException;
import com.example.demo.error.UsuarioNoExisteException;
import com.example.demo.model.Cita;
import com.example.demo.model.CreadencialesCitaConId;
import com.example.demo.model.CredencialesCita;
import com.example.demo.model.CredencialesEditarUser;
import com.example.demo.model.Mascota;
import com.example.demo.model.User;
import com.example.demo.repository.CitasRepository;
import com.example.demo.repository.MascotaRepository;
import com.example.demo.repository.UserRepo;
import com.example.demo.security.JWTUtil;
import com.example.demo.service.CitaService;
import com.example.demo.service.MascotaService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.JsonMappingException;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UserController {

    @Autowired private UserRepo userRepo;
    @Autowired private MascotaRepository mascotaRepo;
    @Autowired private MascotaService mascotaServi;
    @Autowired private CitaService citaServi;
    @Autowired private CitasRepository citaRepo;
    @Autowired private UserService serviUser;
    @Autowired private JWTUtil jwtUtil;
   
   //crear un log
    //private final Logger log = LoggerFactory.getLogger(UserController.class);
    //importado desde org.slf4i
    
  /**Usuario**/
    
    /**
     * Sacar la infromacion del usuario del token. Los datos se sacaran del token
     * @return usuario
     */
    @GetMapping("/cliente")
    public User infoUser () {
    	try {
    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            
            return serviUser.recogerInfoUserPorEmail(email);
    	}catch (AuthenticationException authExc){
       	 	throw new UsuarioNoExisteException() ;
       } 
    }
    
    @GetMapping("/cliente/info")
    public User infoCliente () {
    	try {
    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            
            return serviUser.recogerInfoUserPorEmail(email);
    	}catch (AuthenticationException authExc){
       	 	throw new UsuarioNoExisteException() ;
       } 
    }
  
    /**
     * Editar al cliente. Proceso porque salta inacesible
     * @param cliente
     * @return el token por si cambia la contraseña, pues creamos otra vez el token
     */
    @PutMapping("/cliente")
    public Map<String, Object> editarCliente (@RequestBody CredencialesEditarUser clienteDatosEditado) {
    	try {
    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User usuario = userRepo.findByEmail(email).get();
            serviUser.edit(clienteDatosEditado, usuario);
            String token = jwtUtil.generateToken(usuario.getEmail());

            return Collections.singletonMap("jwt-token", token);
    	}catch (AuthenticationException authExc){
       	 	throw new UsuarioNoExisteException() ;
       } 
    	
    }
    
    /**En proceso
     * Metodo que permite borrar un cliente . Recoge desde el token para borrar el cliente
     * @return
     */
    @DeleteMapping("/cliente")
    public ResponseEntity<?> borrarUsuario(){

        try {
     	String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User usuario = userRepo.findByEmail(email).get();
        serviUser.delete(usuario.getId());
        return ResponseEntity.noContent().build();
        }catch (AuthenticationException authExc){
        	 throw new UsuarioNoExisteException() ;
        } 
    }
    
    
    
    
    
    
 /**---------------------Mascota-------------------------------------**/  
    /**
     *  Con este metodo añadiremos mascota nueva y gracia asu relacion, se le añadira al cliente
     * @param Trae la informacion de la mascota. Comprueba que el usuario
     * @return l amscota nueva
     */
    
    @PostMapping("/cliente/mascota")
    public Mascota addPet(@RequestBody Mascota mascota){
    
       try {
    	String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User usuario = serviUser.recogerInfoUserPorEmail(email);
        
        	if (usuario !=null) {
        		//if(!imagen.isEmpty()) {
        		//	Path directorioRecursos = Paths.get("src//main//resources//static//uploads");
        		//	String rootPath= directorioRecursos.toFile().getAbsolutePath();
        			
        		//}
        		mascotaServi.addMascota(mascota, usuario); 

        		return mascota;
        	}else {
        		throw new UsuarioNoExisteException();
        	}
       }catch (AuthenticationException authExc){
           throw new UsuarioNoExisteException() ;
       }
       // return userRepo.findByEmail(email).get();//.get
    }
    

    /**
     * Con este metodo mostraremos las mascotas del usuario del token
     * Se recoge el email que es unico del token
     * @return lista de mascota del usuario. Si no tiene mascota, sera una lista de mascota vacia
     */
    
    @GetMapping("/cliente/mascota")
    public List<Mascota> mascotasDelUser(){
    	
    	try {
    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User usuario = serviUser.recogerInfoUserPorEmail(email);
            if(usuario != null) {/**Si el usuario no es null**/
            	
   
            	 return mascotaServi.mostrarMascotadeUser(usuario);
            	
            }else {
            	/**No existe el usuario**/
            	 throw new UsuarioNoExisteException() ;
            }
    	}catch (AuthenticationException authExc){
            throw new UsuarioNoExisteException() ;
        }    
 
    }
  
    
    /**
     * Mostramos una mascota por su id
     * @param id
     * @return Mascota
     */
    
    @GetMapping("/cliente/mascota/{id}")
    public Mascota mascotaporId(@PathVariable Long id){
    	
    	try {
    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User usuario = serviUser.recogerInfoUserPorEmail(email);
            	 
            Boolean petExiste = mascotaServi.comprobarporId(id);
            	if (petExiste) {
            		return mascotaServi.encontrarId(id);
            	}else {
            		throw new MascotaNoExistedException();
            	}
            	 
       
    	}catch (AuthenticationException authExc){
    		 throw new UsuarioNoExisteException() ;
        }      
 
    }
  
    
    /**
     * Borrar una mascota por su id
     * @param id
     * @return
     */
     
    @DeleteMapping("/cliente/mascota/{id}")
    public ResponseEntity<?> deletePets(@PathVariable Long id){
    	
    	try {
    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		User usuario = serviUser.recogerInfoUserPorEmail(email);
			
    		Boolean pet= mascotaServi.comprobarporId(id);
			
			if (pet) {//*Si la mascota existe**/
				
				citaServi.deleteByPet(id);
				mascotaServi.borrarFotoMascota(mascotaServi.encontrarId(id));
				mascotaServi.delete(mascotaServi.encontrarId(id));
				
				return ResponseEntity.noContent().build();
			}else {
				throw new MascotaNoExistedException();
			}
			
		} catch (AuthenticationException authExc){
		           throw new UsuarioNoExisteException() ;
		       }
		
        
    }
    
    
    
    
    /**
     * Editar la mascota. Recge usuario del token
     * @param mascota, id mascota
     * @return mascota editada
     */
    
    @PutMapping("/cliente/mascota/{id}")
    public Mascota editarMascota(@RequestBody Mascota mascota, @PathVariable Long id) {
    	
    	try {
    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		User usuario = serviUser.recogerInfoUserPorEmail(email);
    	
			Boolean pet= mascotaServi.comprobarporId(mascota.getId());
			if (pet) {/**Si existe la pet**/
				
				mascotaServi.editarMascota(mascota, usuario);
				
				return mascota;
			}else {/**No existe la mascota**/
				throw new MascotaNoExistedException();
			}
			
    	}catch (AuthenticationException authExc){/**No exite el usuario**/
            throw new UsuarioNoExisteException() ;
        }
        
    }
    
    @GetMapping(value = "/cliente/mascota/throwException")
    public void throwException() {
        throw new IllegalArgumentException("\"La mascota no existe\"");
    }
    
 
    
  /**--Imagenes mascotas--**/
    
    /**Metodo que permite añadir/actualizar la foto de una mascota, que se reconoce por su id.
     * @PostMapping("/cliente/mascota/upload/{id}")
     * @param archivo, que seria MultipartFile, para que permita recoger un fichero, y id de la mascota.
     * @param id
     * @return
     */
    @PostMapping("/cliente/mascota/upload/{id}")
    public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @PathVariable Long id){
    	Map<String, Object> response = new HashMap<>();
    	
    	try {
    		//**Recogemos desde el token de la cabecera, el email.Esto lo hará si existe**/
    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            /**Desde el correo sacamos el usuario**/
    		User usuario = serviUser.recogerInfoUserPorEmail(email);
            /**Comprobamos si el usuario existe*/
    		if(usuario != null) {/**Si el usuario no es null**/
    			Mascota mascota= mascotaServi.encontrarId(id);
            	if(!archivo.isEmpty()) {/*Si el archivo no esta vacio*/
            		//Hay que cambiarle el nombre para que sea unico y no haya conflictos futuros. LLamamos al metodo del servicio de mascotas
            		String nombreArchivo= mascotaServi.cambiarNombreFotoMascota(archivo);
            		//recogemos la ruta de la nueva foto, desde el nombre del archivo creado anteriormente
            		Path rutaArchivo = mascotaServi.recogerRutaDeLaFoto(nombreArchivo);
            		
            		try {
            			/*Mover/copiar  en bite el archivo que hemos subido al servidor a la ruta escogida.*/
						Files.copy(archivo.getInputStream(), rutaArchivo); //asi guardara la iamgen en esa ruta
					} catch (IOException e) {
						response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				   		return new ResponseEntity<Map<String,Object>>(HttpStatus.INTERNAL_SERVER_ERROR);

					}
            		/*Comprobamos que si tiene una foto ya guardada la mascota. Por ello llamaremos al siguiente metodo en servicio de mascota. Si ya tenia una foto, se borrará esta*/
            		mascotaServi.comprobarSiLaMascotaTieneUnaFotoYBorrarlaSiTenia(mascota);
            		/*Añadimos el nombre d ela foto a la mascota*/
            		mascotaServi.incluirALaMascotaLaImagen(mascota, nombreArchivo);
            		/*Guardamos la mascota*/
            		mascotaServi.guardar(mascota);
            		
            		response.put("mascota", mascota);
            		response.put("mensaje", "Has subido correctamente la foto"+ nombreArchivo);
            	}
            	return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
            	
            }else {
            	/**No existe el usuario**/
            	 throw new UsuarioNoExisteException() ;
            }
    	}catch (AuthenticationException authExc){/*Si el usuario no existe*/
            throw new UsuarioNoExisteException() ;
        }    
 
    	
    } 
    
    
    /**
     * Esto sirve para recoger una imagen y mostrar en la web
     * @param nombreFoto
     * @return
     */
    @GetMapping("uploads/img/nombreFoto:.+}") //que tendra un archivo que termina en punto y una expansion
    public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto){
    	//resource importado desde org.springframework.core.io
    	
		Path rutaArchivo = Paths.get("uploads").resolve(nombreFoto).toAbsolutePath();
		Resource recurso= null;
		try {
			recurso = new UrlResource(rutaArchivo.toUri());
		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		}
		
		if (!recurso.exists() && recurso.isReadable()) {
			throw new RuntimeException("No se pudo leerse");
		}
		
		HttpHeaders cabecera = new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename\""+recurso.getFilename()+"\"");
		//Urlresource importado desde org.springframework.core.io
    	return new ResponseEntity<Resource>(recurso,cabecera,HttpStatus.OK);

    }
    
    
    
    
    
    
 
	
	
    
    
    
/**---------------------Fin-Mascota-------------------------------------**/  
    
    
    
    
//**Citas**//
    
    /**
     * Con este metodo añadiremos cita nueva
     * @param Trae la informacion de la cita. La id de mascota. COmprueba que el usuario y la mascota existe
     * @return cita nueva creada
     * dato extra: falta añadir veterinario
     */
    @PostMapping("/cliente/mascota/{id}/cita")
    public Cita addCita(@RequestBody CredencialesCita cita,@PathVariable Long id){
        
        
        try {
        	String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User usuario = serviUser.recogerInfoUserPorEmail(email);
        	
            Boolean pet= mascotaServi.comprobarporId(id);


                if (pet) {//*Si la mascota existe**/
                	Date fechaactual = new Date();
                	
                	//SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                	//Date fechanow=Date.now();
                	java.util.Date fecha = new Date();
                	 //comprueba si la fecha introducida, es anterior a la fecha actual de hoy, en ese caso que salte una exception     
                	if(fecha.after(cita.getFecha())){
                		 throw new FechaanteriorException() ;
                	}else{
                	    System.out.println("Fecha actual mayor");
                	}
            	Cita nuevaCita=citaServi.addCita(cita, usuario, id);
            	if(nuevaCita.getId() == null) {//si es null, es que no se creo la cita . Sera orque la cita ya existia
            		throw new CitaYaExisteException();
            	}
            	return nuevaCita;
            	
        	}else {//Lanza la exception si no existe la amscota
        		throw new MascotaNoExistedException();
        	}
        	   	
        }catch (AuthenticationException authExc){
                throw new UsuarioNoExisteException() ;
            }
        
    }
    
    
   /**
    * Mostrar la lista de citas
    * @param id
    * @return lista de citas
    */
    
    @GetMapping("/cliente/cita")
    public List<Cita> citasDelUsuario(){
    	
    	try {
    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User usuario =serviUser.recogerInfoUserPorEmail(email);
            if(usuario != null) {
            	return citaServi.mostrarCitasUser(usuario);
               
            }else {
            	throw new UsuarioNoExisteException() ;
            }
    	}catch (AuthenticationException authExc){
    		throw new UsuarioNoExisteException() ;
        }      
    }
    
    /**
     * Lista citas de una mascota. En proceso
     */
    //**Mas adelante sacar las citas de solo una amscota**//
    @GetMapping("/cliente/mascota/{id}/cita")
    public List<Cita> citasDelUsuarioDeunaMascota(@PathVariable Long id){
    	
    	try {
    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User usuario = serviUser.recogerInfoUserPorEmail(email);
            if(usuario != null) {/**usuario existe**/
            	
            	Boolean pet= mascotaServi.comprobarporId(id);
                if (pet) {//*Si la mascota existe**/
                	return citaServi.mostrarPorPet(id);
                }else {
                	throw new MascotaNoExistedException();
                }
            	
            }else {
            	throw new UsuarioNoExisteException() ;
            }
    	}catch (AuthenticationException authExc){
    		throw new UsuarioNoExisteException() ;
        }      
    }
    /**
	   * Devuelve la lista de usuario con el rol de veterinario
	   * @return lista de veterinario
	   */
	  @GetMapping("/cliente/veterinario")
	    public List<User> listarVeterinarios () {
	    	try {
	    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	            
	            return serviUser.findAllVeterinarios();
	    	}catch (AuthenticationException authExc){
	       	 	throw new UsuarioNoExisteException() ;
	       } 
	    }
	  
    
    /**
     * Sacar informacion de una cita
     * @param Id de la mascota(idM) y la id de la cita(idC)
     * @return Los datos de la cita
     */
    @GetMapping("/cliente/mascota/{idM}/cita/{idC}")
    public Cita infoCita(@PathVariable Long idM, @PathVariable Long idC){
    	
    	try {
    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User usuario = serviUser.recogerInfoUserPorEmail(email);
            if(usuario != null) {/**si el usuario no es null**/
            	
            	if(mascotaServi.comprobarporId(idM)){/**Si existe la mascota**/
            		if(citaServi.comprobarExistenciaCita(idC)) {/**Si la cita existe**/
            			return citaServi.encontrarCitaId(idC);
            		}else {
            			throw new CitaExistedException(idC);
            		}
            	}
            	else{/**si la mascota no exista**/
            		throw new MascotaNoExistedException();
            	} 
           }
           else {/**si el usuario no existe**/
        	   throw new UsuarioNoExisteException();
            }
    	}catch (AuthenticationException authExc){
            throw new UsuarioNoExisteException();
        }      
    }
    
    /**
     * Borrar cita 
     * @param id
     * @return
     */
    
    @DeleteMapping("/cliente/mascota/{idM}/cita/{id}")
    public ResponseEntity<?> deleteCita(@PathVariable Long idM,@PathVariable Long id){
    	
    	try {
    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		 User usuario = serviUser.recogerInfoUserPorEmail(email);
			if(mascotaServi.comprobarporId(idM)) {
				Boolean cita = citaServi.comprobarExistenciaCita(id);
				if (cita) {/**Si existe la cita**/
					
					Cita citaSeleccionada= citaServi.encontrarCitaId(id);/**Recogemos la cita**/
					citaServi.delete(usuario,citaSeleccionada);
					
					return ResponseEntity.noContent().build();
				}else {
					throw new CitaExistedException(id);
				}
			}else {
				throw new MascotaNoExistedException();
			}
    		
		
		}catch (AuthenticationException authExc){
            throw new UsuarioNoExisteException();
        } 
        
    }
    
    /**
     * Editar una cita
     * @param mascota
     * @return cita editada
     */
    @PutMapping("/cliente/mascota/{id}/cita/{idC}")
    public Cita editarMascota(@RequestBody CreadencialesCitaConId cita,@PathVariable Long id, @PathVariable Long idC) {
    	
    	try{
    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       	 	User usuario = serviUser.recogerInfoUserPorEmail(email);
       	 if (usuario == null) {
       		 throw new UsuarioNoExisteException();
 		} else {

 			Boolean mascotaExiste = mascotaServi.comprobarporId(id);
 			if (mascotaExiste) {/**Si existe la cita**/
 				
 				if(citaServi.comprobarExistenciaCita(idC)) {
 					return citaServi.editarCita(cita, idC, usuario, id);
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
    
    
    
    
    
    
    /**Control de exception**/
    /** Exception de existencia de usuario**/
    /**
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(UsuarioNoExisteException.class)
	public ResponseEntity<ApiError> handleUserNoEncontrado(UsuarioNoExisteException  ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.NOT_FOUND);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}
	  @ExceptionHandler(FechaanteriorException.class)
		public ResponseEntity<ApiError> handleFechaanterior(FechaanteriorException  ex) {
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

	/**-------Mascota-----------------**/
	   //*Exception de cuando la mascota no existe**//
    @ExceptionHandler(MascotaNoExistedException.class)
	public ResponseEntity<ApiError> handleMascotaNoEncontrado(MascotaNoExistedException  ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.NOT_FOUND);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}
	
    //*Exception cuando no ha podido crear la mascota**//
    @ExceptionHandler(NoCreasMascotaException.class)
	public ResponseEntity<ApiError> handleMascotaCrearNoEncontrado(NoCreasMascotaException  ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.NOT_FOUND);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}
    
    
    //**Cita**//
    //*Exception cuando no existe la cita**//
    @ExceptionHandler(CitaExistedException.class)
	public ResponseEntity<ApiError> handleCitaNoEncontrado(CitaExistedException  ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.NOT_FOUND);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}
    
    @ExceptionHandler(CitaYaExisteException.class)
	public ResponseEntity<ApiError> handlecitaYaExisteException(CitaYaExisteException  ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.CONFLICT);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
	}
    
}