package com.nelioalves.cursomc.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nelioalves.cursomc.domain.Produto;
import com.nelioalves.cursomc.domain.dto.ProdutoDTO;
import com.nelioalves.cursomc.resources.utils.URL;
import com.nelioalves.cursomc.services.ProdutoService;

import javassist.tools.rmi.ObjectNotFoundException;

@RestController
@RequestMapping(value="/produtos")
public class ProdutoResource {

	@Autowired
	private ProdutoService service;
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<?> find(@PathVariable Integer id) throws ObjectNotFoundException {
		Produto obj = service.find(id);
		return ResponseEntity.ok(obj);
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<Page<ProdutoDTO>> findPage( 
			@RequestParam(name="nome", defaultValue = "") String nome,
			@RequestParam(name="categorias", defaultValue = "") String categorias,
			@RequestParam(name="page", defaultValue = "0") Integer page, 
			@RequestParam(name="linesPerPage", defaultValue = "24") Integer linesPerPage, 
			@RequestParam(name="orderBy", defaultValue = "nome") String orderBy, 
			@RequestParam(name="direction", defaultValue = "ASC") String direction
	) {
		String nomeDecoded = URL.decodeParam(nome);
		
		List<Integer> categoriasIds = URL.decodeIntList(categorias);
		Page<Produto> list = service.search(nomeDecoded, categoriasIds, page, linesPerPage, orderBy, direction);
		Page<ProdutoDTO> listDTO = list.map(obj -> new ProdutoDTO(obj)); 

		return ResponseEntity.ok().body(listDTO);
	}
	
}
