package com.example.demo.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

@Repository 
public interface UserRepo extends JpaRepository<User, Long> {
   //Método para obtener un usuario por su email
	public Optional<User> findByEmail(String email);
	
	public Optional<List<User>> findByRole(String role);
	
	
}
