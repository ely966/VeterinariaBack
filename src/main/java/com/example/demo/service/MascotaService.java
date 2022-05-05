package com.example.demo.service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.error.MascotaExistedException;
import com.example.demo.model.Mascota;
import com.example.demo.model.User;
import com.example.demo.repository.MascotaRepository;

@Primary
@Service("MascotaService")
public class MascotaService {
	
	@Autowired private MascotaRepository mascotaRepo;
	
	/**
	 * Añadir una mascota
	 * @param mascota
	 * @param cliente
	 * @return mascota añadida
	 */
	public Mascota addMascota(Mascota mascota, User cliente) {
		Mascota newMascota= new Mascota();
		newMascota.setNombre(mascota.getNombre());
		newMascota.setTipo(mascota.getTipo());
		newMascota.setRaza(mascota.getRaza());
		newMascota.setEdad(mascota.getEdad());
		newMascota.setUsuario(cliente);
		
		mascotaRepo.save(newMascota);
		List<Mascota>pets=mascotaRepo.findAll();
		return newMascota;
	}
	/**
	 * Encontrar una mascota por su id
	 * @param id
	 * @return Mascota
	 */
	public Mascota encontrarId(Long id) {
		List<Mascota>pets=mascotaRepo.findAll();
		Mascota pet= new Mascota();
		Mascota mascotaseleccionada = mascotaRepo.findById(id).orElse(null);
	
		Mascota mascotase= mascotaRepo.findById(id).get();
		for (int i=0; i< mascotaRepo.count();i=i+1) {
			if(pets.get(i).getId() == id) {
				pet = pets.get(i);
			}
		}
		return mascotase;
		
	}
	/**
	 * Encontrar una mascota por su id
	 * @param id
	 * @return booleean
	 */
	public Boolean comprobarporId(Long id) {
		
		return mascotaRepo.existsById(id);
		
		
	}
	/**
	 * Guardar una mascota 
	 * @param id
	 * @return 
	 * @return booleean
	 */
	public  void guardar(Mascota mascota) {
		
		mascotaRepo.save(mascota);
		
		
	}

	/**
	 * Borra una mascota
	 * @param mascota
	 * @return mascota borrada
	 */
	public Mascota delete (Mascota mascota) {
		if(mascotaRepo.existsById(mascota.getId())) {
			mascota.setUsuario(null);
			mascotaRepo.save(mascota);
			mascotaRepo.deleteById(mascota.getId());
			return mascota;
		}
		else {
			throw new IllegalArgumentException("\"La mascota no existe, y por lo tanto no puede ser borrada\"");
		}
	}
	
	/**
	 * Mostrar las mascotas del usuario
	 * @param cliente
	 */
	public void mostrarMascotaPorUsuario(User cliente){
		List<Mascota> mascotasGeneral =mascotaRepo.findAll();
		List<Mascota> mascotasUser=new ArrayList();
		
		for (int i =0; mascotasGeneral.size() > i ; i=i+1) {
			if(mascotasGeneral.get(i).getUsuario().equals(cliente)) {
				mascotasUser.add(mascotasGeneral.get(i));
			}
		}
		
	}

	
	/**
	 * Otra forma de obtener las mascotas del cliente
	 * @param cliente
	 * @return lista de mascota del cliente
	 */
	public List<Mascota> mostrarMascotadeUser(User cliente){
		List<Mascota> mascotasUser=cliente.getMascotas();
		return mascotasUser;
	}
	
	/**
	 * Editar una mascota
	 * @param mascota
	 * @return mascota editada
	 */
	public Mascota editarMascota(Mascota mascota, User usuario) {
		List<Mascota> mascotasGeneral =mascotaRepo.findAll();
		mascota.setUsuario(usuario);
		for (int i =0; mascotasGeneral.size() > i ; i=i+1) {
			if(mascotasGeneral.get(i).getId() == mascota.getId()) {
				mascotaRepo.save(mascota);
			}
		}
		return mascota;

		
	}
	
	/**-------------------------Imagenes de Mascotas---------------**/
	
	/**
	 * Metodo que permite cambiarle el nombre del archivo, para que si coinciden nombre con otr aimagen o contiene espacios, evitar errores y se guarde correctamente.
	 * @param archivo
	 * @return un string que seria el nombre del archivo
	 */
	public String cambiarNombreFotoMascota (MultipartFile archivo) {
		//Hayq ue cambairle el nombre para que sea unico y no haya conflictos futuros
		//reemplazar los espacios en blanco por nada
		String nombreArchivo= UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename().replace(" ", "");
		return nombreArchivo;
	}
	
	/*================*/
	/**
	 * Metodo que recogerá la ruta de la nueva foto que será añadida
	 * @param nombreArchivo
	 * @return la ruta 
	 */
	public Path recogerRutaDeLaFoto(String nombreArchivo) {
		//recoge el directorio donde estas las fotos
		//importamos desde 
		Path directorioRecursos = Paths.get("uploads");
		//recogemos la ruta de la nueva foto
		Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();
		return rutaArchivo;
	}
	
	/*================*/
	/**
	 * Este metodo comprueba si la mascota, sí tiene una foto anterior, y sí tenía una foto anterior, al borrará
	 * @param mascota
	 */
	public void comprobarSiLaMascotaTieneUnaFotoYBorrarlaSiTenia(Mascota mascota) {
		String nombreFotoAnterior= mascota.getFoto();
		if(nombreFotoAnterior != null && nombreFotoAnterior.length() > 0) {
			//si la mascota tenia una foto anterior, la borramos de antes para no tener una foto huerfana extra
			borrarFotoMascota(mascota);
			
		}
	}
	/*================*/
	
	/**
	 * Borrar la imagen de la mascota
	 * @param mascota
	 */
	public void borrarFotoMascota (Mascota mascota) {
		//hayq ue borrar la foto anterior, si ya tiene una. Esto añadir antes de borrar una mascota que borre su imagen
		String nombreFotoAnterior= mascota.getFoto();
		if(nombreFotoAnterior != null && nombreFotoAnterior.length() > 0) {
			//obtenermos la ruta de la imagen
			//Importar path de java.nio.file
			Path rutaFotoanterior = Paths.get("uploads").resolve(nombreFotoAnterior).toAbsolutePath();
			//lo convertimos en un archivos la foto anterior
			//importamos de java.io
			File archivoFotoAnterior = rutaFotoanterior.toFile();
			//comprobamos que hay un archivo que existe y se puede leer
			if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
				archivoFotoAnterior.delete();
			}
		}
	}
	
	/*================*/
	/**
	 * Recoge la mascota y el nombre del fichero, y añade a la mascota, el nombre del archivo.
	 * @param mascota
	 * @param nombreArchivo
	 * @return mascota con la foto incluida
	 */
	public Mascota incluirALaMascotaLaImagen(Mascota mascota, String nombreArchivo) {
		mascota.setFoto(nombreArchivo);
		return mascota;
		
	}
	
	
	
	
}
