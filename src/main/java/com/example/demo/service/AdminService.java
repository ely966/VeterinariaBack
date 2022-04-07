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
