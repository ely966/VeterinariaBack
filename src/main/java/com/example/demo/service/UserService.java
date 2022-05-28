package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.CredencialesEditarUser;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;


@Primary
@Service("UserService")
public class UserService {
	
	@Autowired
	private UserRepo userRepo;
	
    @Autowired private PasswordEncoder passwordEncoder;

    
    /**Comunes para todos los tipos de usuarios**/

    
    /**
     * Agregamos un usuario tanto que sea cliente, administrador o veterinario
     * Traemos la contraseña la cual encriptamos antes de guardarlos. y le añadimos el roll
     * Tras añadirle la contraseña encriptada y su roll correspondiente guardamos el usuario.
     * @Param recibe el usuario y roll
     * @return devolvemos usuario
     */
    public 	User agregarUser(User usuario, String roll) {
    	/**encriptamos la contraseña**/
    	String encodedPass = passwordEncoder.encode(usuario.getPassword());
 	   	/**Añadimos la contraseña encriptada al usuario**/
    	usuario.setPassword(encodedPass);
    	/**Añadimos su roll*/
    	usuario.setRole(roll);
    	userRepo.save(usuario);
    	return usuario;
    	
    }
	
	/**
	 * Sacar la informacion del usuario desde el email
	 * @param email
	 * @return usuario
	 */
	public User recogerInfoUserPorEmail (String email) {
		return userRepo.findByEmail(email).get();
	
	}
	/**
	 * Borrar usuario 
	 * @param id
	 * @return
	 */
	public User delete (Long id) {
		if(userRepo.existsById(id)) {
			
			User user = userRepo.findById(id).get();
			user.setMascotas(null);
			user.setCitas(null);
			userRepo.save(user);
			userRepo.deleteById(id);
			return user;
		}else {
			return null;
		}
	}
	
	/**
	 * Editar un usuario
	 * @param datosUserNuevo
	 * @param usuario
	 * @return usuario
	 */
	public User edit (CredencialesEditarUser datosUserNuevo, User usuario) {
		//datos nuevos
		usuario.setDireccion(datosUserNuevo.getDireccion());
		usuario.setNombre(datosUserNuevo.getNombre());
		usuario.setTelefono(usuario.getTelefono());
		if(datosUserNuevo.getPassword() !=null){
			/*Encriptar la contraseña*/
			String passEncrip = passwordEncoder.encode(datosUserNuevo.getPassword());
			usuario.setPassword(passEncrip);
		}
		
		
		userRepo.save(usuario);
		return usuario;
			
		}
	
	
	
	
	/**
	 * Encontrar por un usuario por su email
	 * @param users
	 * @param emailNuevoCorreo
	 * @return
	 */
	public Boolean findByEmail(List<User> users, String emailNuevoCorreo){
		Boolean encontrado =false;
		int i=0;
		while ( i<users.size() && !encontrado) {
			if(users.get(i).getEmail().equals(emailNuevoCorreo)) {
				//Si el correo es encontrado. No puede repetirse el correo
				encontrado=true;
				return true;
				
			}
			else {
				i=i+1;
			}
			}
		return false;
	}
	
	//**=========================================================**//
	/** CLIENTE **/
    
    /**
     * Mostrar todos los usuarios
     * @return lista de usuario
     */
	public List<User> findAll(){
		return userRepo.findAll();
	}
	
	/**
	 * Mostrar usuario por su id
	 * @param id
	 * @return usuario
	 */
	public User findById (Long id) {
		return userRepo.findById(id).get();
	}
	/**Añadir un usuario
	 * 
	 * @param user
	 * @return usuario
	 */
	public User add (User user) {
		return userRepo.save(user);
	}


	  /**
     * Mostrar todos los usuarios con rol veterinario
     * @return lista de veterinario
     */
	public List<User> findAllCliente(){
		return userRepo.findByRole("CLIENTE").get();
	}
	
	
	
	
//**=========================================================**//
	//**Rol Administrador **//
	
 
    


    
    /**
	 * Mostrar administrador  por su id
	 * @param id
	 * @return Admin que le corresponda el id
	 */
	public User findByIdAdmin (Long id) {
		return userRepo.getById(id);
	}
	
	
    /**
     * Mostrar todos los usuarios con rol administrador
     * @return lista de administradores
     */
	public List<User> findAllAdmin(){
		return userRepo.findByRole("ADMIN").get();
	}
	
	

	//**=========================================================**//
	//**Veterinario**/
	  /**
     * Mostrar todos los usuarios con rol veterinario
     * @return lista de veterinario
     */
	public List<User> findAllVeterinarios(){
		return userRepo.findByRole("VETERINARIO").get();
	}

	
	
	  /**
		 * Borra un veterinario
		 * @param mascota
		 * @return mascota borrada
		 */
		public User deleteVeterinario (User veterinario) {
			//antes de borrar el veterinario
			//veteriServi.deleteVeterinario(veterinario);
			return veterinario;
			
		}

	//encriptar pass
	
	public String encriptar(String pass) {
		 
		 String encodedPass = passwordEncoder.encode(pass);
		 return encodedPass;
	}
	
	
}
