package com.example.demo;


import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;
import com.example.demo.service.UserService;

@SpringBootApplication
public class ProyectoApiVeterinaria2Application {
    @Autowired private PasswordEncoder passwordEncoder;
    
	public static void main(String[] args) {
		SpringApplication.run(ProyectoApiVeterinaria2Application.class, args);
	}

	
	//User(String nombre, String email, String password, String role)
	//Creamos desde el inicio administradores
	/**Creamos al inicio administradores
	 * @Bean
	 * @param userRepo
	 * @return
	 */
	@Bean
	CommandLineRunner iniciarAdmins( UserRepo userRepo) {return (args) ->{
		/**Crear por defecto 2 administradores**/
		//String encodedPassword = encoder.encode("UserPassword");  
		userRepo.saveAll(Arrays.asList(new User("ely", "aDveTminsdgddgfg@outlook.com", "calle real", passwordEncoder.encode("adminVeterinario"), "ADMIN"),
							new User("admin", "admin@gmail.com", "Calle real",passwordEncoder.encode("admin"), "ADMIN"),
							/**Crear por defecto 3 veterinarios**/
							new User("Juan", "juan@gmail.com", "Calle real",passwordEncoder.encode("admin"), "VETERINARIO","Diagnosticos"),
							new User("luisa", "luisa@gmail.com", "Calle real",passwordEncoder.encode("luisa"), "VETERINARIO","Operaciones"),
							new User("Mariana", "mariana@gmail.com", "Calle Virgen Macarena",passwordEncoder.encode("luisa"), "VETERINARIO","General"),
							/**Crear por defecto 2 cliente**/
							new User("lucinda", "lucinda23@gmail.com", "Calle real",passwordEncoder.encode("lucinda"), "CLIENTE")
							
							));
		};
		
							
	}
	
}

