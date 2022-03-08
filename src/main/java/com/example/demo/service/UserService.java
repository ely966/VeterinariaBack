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
			User usuarioEditado = new User();
			
			usuarioEditado.setId(usuario.getId());
			usuarioEditado.setMascotas(usuario.getMascotas());
			usuarioEditado.setCitas(usuario.getCitas());
			usuarioEditado.setEmail(usuario.getEmail());
			//usuarioEditado.setDireccion(usuario.getUsername());
			usuarioEditado.setPassword(usuario.getPassword());
			//datos nuevos
			usuarioEditado.setDireccion(datosUserNuevo.getDireccion());
			usuarioEditado.setNombre(datosUserNuevo.getNombre());
			userRepo.save(usuarioEditado);
			return usuarioEditado;
			
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
