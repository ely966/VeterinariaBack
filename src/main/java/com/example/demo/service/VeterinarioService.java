package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Cita;
import com.example.demo.model.Mascota;
import com.example.demo.model.User;
import com.example.demo.repository.CitasRepository;
import com.example.demo.repository.MascotaRepository;
import com.example.demo.repository.UserRepo;

@Primary
@Service("VeterinarioService")
public class VeterinarioService {
	@Autowired
	private UserRepo userRepo;
	
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private CitaService citaServi;
    @Autowired private CitasRepository citaRepo;
    @Autowired private UserService serviUser;
    
    
	  /**
		 * Borra un veterinario. Por lo tanto las citas relacionadas
		 * @param usuario con rol de veterinario, que se debe borrar
		 * @return mascota borrada
		 */
		public User deleteVeterinario (User veterinario) {
			//antes de borrar el veterinario
			
			//---Borrar primero las citas-----
			//Obteneos las citas del veterinario
			List<Cita>citasVeterinario=citaServi.mostrarPorVeterinario(veterinario.getId());
			//borramos todas als citas
			for (int i =0; citasVeterinario.size() > i; i=i+1) {//Vamos borrando las citas
				citaServi.delete(citasVeterinario.get(i).getCliente(), citasVeterinario.get(i));//vamos borrando la cita tanto del su cliente como la cita en si
				
				}
			//Una vez borrada las citas del veterinario.
			//por si acaso tenia algo vaciamos el usuario y guardamos
			veterinario.setMascotas(null);
			veterinario.setCitas(null);
			userRepo.save(veterinario);
			//ahora si borramos el veterinario
			userRepo.deleteById(veterinario.getId());
			return veterinario;
			
			}
			
		
			
			
    /**
     * Segun el ipo del veterinario , sacara las funciones que este ofrece
     * @param tipoVeterinario
     * @return
     */
    public List<String> funciones(String tipoVeterinario) {
    	List<String> funciones = new ArrayList();
    	if(tipoVeterinario.equals("diagnostico")) {
    		funciones.add("Vacunar");
    		funciones.add("Revisión");
    		funciones.add("Analíticas");
    		funciones.add("Desparasitar");
    	}
    	else if (tipoVeterinario.equals("operacion")){
    		funciones.add("Operar");
    		funciones.add("Revisión tras operar");
    		funciones.add("Prueba");
    	}
    	return funciones;
    }
    
  
	
    /**
     * Busca las citas que tenga el veterinario
     * @param id del veterinario
     * @return lista de citas que debe atender el veterinario
     */
    public List<Cita> citasDelVeterinario(Long idVeterinario) {
    	List<Cita> citasVeterinario = new ArrayList();
    	citasVeterinario = citaRepo.findByIdVeterinario(idVeterinario).get();
    	return citasVeterinario;
    }

}
