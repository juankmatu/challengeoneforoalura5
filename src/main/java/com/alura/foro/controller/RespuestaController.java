package com.alura.foro.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alura.foro.persistence.DTO.ResponsePerfil;
import com.alura.foro.persistence.DTO.ResponseRespuestas;
import com.alura.foro.persistence.DTO.ResponseTopico;
import com.alura.foro.persistence.entity.Respuesta;
import com.alura.foro.persistence.entity.Topico;
import com.alura.foro.service.PerfilService;
import com.alura.foro.service.RespuestaService;
import com.alura.foro.service.TopicoService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/foro")
@AllArgsConstructor
public class RespuestaController {
	
	private PerfilService perfilService;
	private RespuestaService respuestaService;
	private TopicoService topicoService;
	

	@PostMapping("/topico/{idtopico}/respuesta")
	public ResponseEntity<ResponseTopico> saveRespuesta(@RequestBody Respuesta respuesta, @PathVariable("idtopico") long idtopico){


		respuesta.setIdtopico(idtopico);
		respuesta.setPerfil(perfilService.getPerfilById(respuesta.getPerfil().getId()));
		
		respuesta = respuestaService.saveRespuesta(respuesta);
		
		Topico topico = topicoService.getTopicoById(idtopico);
		ResponsePerfil perfil = new ResponsePerfil(
				topico.getPerfil().getNombrecompleto(), 
				topico.getPerfil().getEmail(), 
				topico.getPerfil().getUsuario().getNombreusuario());
		
		
		List<ResponseRespuestas> responseRespuestas = new ArrayList<ResponseRespuestas>();
		
		topico.getRespuestas().forEach(respuestaTopico -> {
			
			responseRespuestas.add(new ResponseRespuestas(
					respuestaTopico.getId(),
					respuestaTopico.getMensaje(),
					respuestaTopico.getIdtopico(),
					respuestaTopico.getSolucion(), new ResponsePerfil(
							respuestaTopico.getPerfil().getNombrecompleto(), 
							respuestaTopico.getPerfil().getEmail(), 
							respuestaTopico.getPerfil().getUsuario().getNombreusuario())));
		});
		
		
		ResponseTopico responseTopico = new ResponseTopico(
				topico.getId(), 
				topico.getTitulo(), 
				topico.getMensaje(), 
				perfil, responseRespuestas);
		
		return new ResponseEntity<ResponseTopico>(responseTopico, HttpStatus.CREATED);
	}
	
	@GetMapping("/respuesta/{id}")
	public ResponseEntity<Respuesta> getRespuestaById(@PathVariable("id") long id){
		return new ResponseEntity<Respuesta>(respuestaService.getRespuestaById(id), HttpStatus.OK);
	}
	@PutMapping("/respuesta/{resp_id}")
	public ResponseEntity<ResponseRespuestas> updateRespuesta(@PathVariable("resp_id") long resp_id
												  ,@RequestBody Respuesta respuesta){
		
		Respuesta existenteRespuesta = respuestaService.getRespuestaById(resp_id);
				
		ResponseRespuestas responseRespuestas;
		
		var vHttpStatus = HttpStatus.NOT_MODIFIED;
		if(existenteRespuesta.getPerfil().getId() == respuesta.getPerfil().getId())
		{
			respuesta = respuestaService.updateRespuesta(respuesta, resp_id);
			
			existenteRespuesta.setMensaje(respuesta.getMensaje());
			vHttpStatus = HttpStatus.OK;
			
		}
		
		respuesta.setPerfil(perfilService.getPerfilById(respuesta.getPerfil().getId()));
		
		responseRespuestas = new ResponseRespuestas(
				respuesta.getId(),
				respuesta.getMensaje(),
				respuesta.getIdtopico(),
				respuesta.getSolucion(), new ResponsePerfil(
						respuesta.getPerfil().getNombrecompleto(), 
						respuesta.getPerfil().getEmail(), 
						respuesta.getPerfil().getUsuario().getNombreusuario()));
					
		System.out.println(existenteRespuesta);
		
		return new ResponseEntity<ResponseRespuestas>(responseRespuestas, vHttpStatus);
	}
	
	@DeleteMapping("/respuesta/{id}")
	public ResponseEntity<String> deleteRespuesta(@PathVariable("id") long id){
		
		// delete respuesta from DB
		respuestaService.deleteRespuesta(id);
		
		return new ResponseEntity<String>("Respuesta deleted successfully!.", HttpStatus.OK);
	}
	
	
}
