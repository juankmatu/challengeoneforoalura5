package com.alura.foro.service;

import java.util.List;


import org.springframework.stereotype.Service;

import com.alura.foro.exceptions.ResourceNotFoundException;
import com.alura.foro.persistence.entity.Respuesta;
import com.alura.foro.persistence.repository.RespuestaRepository;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class RespuestaService {
	
	
	private RespuestaRepository respuestaRepository;
	
	
	public Respuesta saveRespuesta(Respuesta respuesta) {
		return respuestaRepository.save(respuesta);
	}
	
	public List<Respuesta> getAllRespuestas() {
		return respuestaRepository.findAll();
	}
	
	public Respuesta getRespuestaById(long id) {
		return respuestaRepository.findById(id).orElseThrow(() -> 
						new ResourceNotFoundException("Respuesta", "Id", id));
		
	}

	
	public Respuesta updateRespuesta(Respuesta respuesta, long id) {
			
			// we need to check whether table with given id is exist in DB or not
			Respuesta existingRespuesta = respuestaRepository.findById(id).orElseThrow(
						() -> new ResourceNotFoundException("Respuesta", "Id", id)); 
				
			existingRespuesta.setPerfil(respuesta.getPerfil());
			existingRespuesta.setMensaje(respuesta.getMensaje()); 
			
		
			respuestaRepository.save(existingRespuesta);
			return existingRespuesta;
		}
	
	public void deleteRespuesta(long id) {
			
	
		respuestaRepository.findById(id).orElseThrow(() -> 
									new ResourceNotFoundException("Respuesta", "Id", id));
		respuestaRepository.deleteById(id);
		}
}
