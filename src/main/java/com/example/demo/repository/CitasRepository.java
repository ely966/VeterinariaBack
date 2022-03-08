package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Cita;
import com.example.demo.model.User;

@Repository
public interface CitasRepository extends JpaRepository<Cita, Long>{
	
}
