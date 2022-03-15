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
    //@Autowired private PasswordEncoder passwordEncoder;
    
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
	//@Bean
	//CommandLineRunner iniciarAdmins( UserRepo userRepo) {return (args) ->{
		
		//String encodedPassword = encoder.encode("UserPassword");  
		//userRepo.saveAll(Arrays.asList(new User("ely", "ely@gmail.com", "calle Murrillo", passwordEncoder.encode("ely"), "admin"),
		//					new User("admin", "admin@gmail.com", "Calle real",passwordEncoder.encode("admin"), "admin")));
		//};
							
	//}
	
}

