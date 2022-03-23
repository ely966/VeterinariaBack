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

    /** ROL CLIENTE **/
    
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
		return userRepo.getById(id);
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
	 * Sacar la informacion del usuario desde el email
	 * @param email
	 * @return usuario
	 */
	public User recogerInfoUserPorEmail (String email) {
		return userRepo.findByEmail(email).get();
	}
	
	/**
	 * Editar un usuario
	 * @param datosUserNuevo
	 * @param usuario
	 * @return
	 */
	public User edit (CredencialesEditarUser datosUserNuevo, User usuario) {
			
			//datos nuevos
			usuario.setDireccion(datosUserNuevo.getDireccion());
			usuario.setNombre(datosUserNuevo.getNombre());
			userRepo.save(usuario);
			return usuario;
			
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
	 * Encontrar por email
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
	
	/**Rol Administrador **/
	
    /**
     * Mostrar todos los usuarios con rol administrador
     * @return lista de administradores
     */
	public List<User> findAllAdmin(){
		return userRepo.findByRole("ADMIN").get();
	}
	
	/**
	 * Mostrar administrador  por su id
	 * @param id
	 * @return Admin que le corresponda el id
	 */
	public User findByIdAdmin (Long id) {
		return userRepo.getById(id);
	}
	
	
	
	
	//encriptar pass
	
	public String encriptar(String pass) {
		 
		 String encodedPass = passwordEncoder.encode(pass);
		 return encodedPass;
	}
	
	public void init () {
		User userEjemplo= new User("lucinda","lucinda@gmail.com","a");
		String encodedPass = passwordEncoder.encode(userEjemplo.getPassword());
		userEjemplo.setPassword(encodedPass);
	    userRepo.save(userEjemplo);
	    
		User userEjwmplo2= new User("Pedro","pedro@gmail.com","a");
		String encodedPass2 = passwordEncoder.encode(userEjemplo.getPassword());
		userEjemplo.setPassword(encodedPass2);
	    userRepo.save(userEjwmplo2);
	}
}
