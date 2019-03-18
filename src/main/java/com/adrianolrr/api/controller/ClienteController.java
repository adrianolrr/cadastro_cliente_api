package com.adrianolrr.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.adrianolrr.api.model.Cliente;
import com.adrianolrr.api.model.Geo;
import com.adrianolrr.api.model.Weather;
import com.adrianolrr.api.repository.ClienteRepository;

@RestController
@RequestMapping({"/clientes"})
public class ClienteController {

  private ClienteRepository repository;

  ClienteController(ClienteRepository clienteRepository) {
      this.repository = clienteRepository;
  }
  
  @GetMapping
  public List findAll(HttpServletRequest request){
	 
	  
    return repository.findAll();
  }
  
  @GetMapping(path = {"/{id}"})
  public ResponseEntity<Cliente> findById(HttpServletRequest request,
		  									@PathVariable long id){
	  
    return repository.findById(id)
            .map(record -> ResponseEntity.ok().body(record))
            .orElse(ResponseEntity.notFound().build());
  }
  
  @PostMapping
  public Cliente create(HttpServletRequest request, 
		  				@RequestBody Cliente cliente){
	  System.out.println(request.getRemoteAddr());
	  
	  String ip = request.getRemoteAddr();
	  
	  String urlRestGeo = "https://ipvigilante.com/8.8.8.8";
	  
	  RestTemplate restTemplate = new RestTemplate();
	  ResponseEntity<Object> responseGeo  = restTemplate.getForEntity("https://ipvigilante.com/8.8.8.8", Object.class);
	  
	  Object objectsGeo = responseGeo.getBody();
	  MediaType contentTypeGeo = responseGeo.getHeaders().getContentType();
	  HttpStatus statusCodeGeo = responseGeo.getStatusCode();
	  
	  String cidade = null;
	  
	  String urlRestWeather = "https://www.metaweather.com/api/location/search/?query=london";
	  
	  ResponseEntity<Object> responseWeather  = restTemplate.getForEntity(urlRestWeather, Object.class);
	  
	  Object objectsRestWeather = responseWeather.getBody();
	  MediaType contentTypeRestWeather = responseWeather.getHeaders().getContentType();
	  HttpStatus statusCodeRestWeather = responseWeather.getStatusCode();


	  
      return repository.save(cliente);
  }

  @PutMapping(value="/{id}")
  public ResponseEntity<Cliente> update(HttpServletRequest request,
		  								@PathVariable("id") long id,
                                        @RequestBody Cliente cliente){
	 
	  
    return repository.findById(id)
        .map(record -> {
            record.setNome(cliente.getNome());
            record.setIdade(cliente.getIdade());
            Cliente	updated = repository.save(record);
            return ResponseEntity.ok().body(updated);
        }).orElse(ResponseEntity.notFound().build());
  }
  
  @DeleteMapping(path ={"/{id}"})
  public ResponseEntity<?> delete(HttpServletRequest request,
		  						@PathVariable("id") long id) {
	 
	  
    return repository.findById(id)
        .map(record -> {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
  }

}
