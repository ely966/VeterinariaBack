package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Cita;
import com.example.demo.model.CredencialesEditarUser;
import com.example.demo.model.User;
import com.example.demo.repository.CitasRepository;
import com.example.demo.repository.UserRepo;


@Primary
@Service("AdminService")

public class AdminService {
	
	@Autowired
	private UserRepo userRepo;
	
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private CitasRepository citaRepo;
    @Autowired private VeterinarioService veteriServi;
    /**
     * Agregamos un administrador o veterinario
     * Traemos la contraseña la cual encriptamos antes de guardarlos. y le añadimos el roll
     * Tras añadirle la contraseña encriptada y su roll correspondiente guardamos el usuario.
     * @Param recibe el usuario y roll(admin o veterinario)
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
	
	

	
	
	  /**
     * Mostrar todos los usuarios con rol veterinario
     * @return lista de veterinario
     */
	public List<User> findAllVeterinarios(){
		return userRepo.findByRole("VETERINARIO").get();
	}
	  /**
     * Mostrar todos los usuarios con rol veterinario
     * @return lista de veterinario
     */
	public List<User> findAllCliente(){
		return userRepo.findByRole("CLIENTE").get();
	}
	
	
	  /**
		 * Borra un veterinario
		 * @param mascota
		 * @return mascota borrada
		 */
		public User deleteVeterinario (User veterinario) {
			//antes de borrar el veterinario
			veteriServi.deleteVeterinario(veterinario);
			return veterinario;
			
		}
	
	
	
	
	
	//encriptar pass
	
	public String encriptar(String pass) {
		 
		 String encodedPass = passwordEncoder.encode(pass);
		 return encodedPass;
	}
	
	
}
