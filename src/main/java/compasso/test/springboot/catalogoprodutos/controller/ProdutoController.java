package compasso.test.springboot.catalogoprodutos.controller;

import compasso.test.springboot.catalogoprodutos.model.Produto;
import compasso.test.springboot.catalogoprodutos.model.dto.ProdutoDTO;
import compasso.test.springboot.catalogoprodutos.model.dto.ProdutoSaveDTO;
import compasso.test.springboot.catalogoprodutos.service.ProdutoService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProdutoController {
	private final ProdutoService produtoService;

	public ProdutoController(ProdutoService produtoService) {
		this.produtoService = produtoService;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProdutoDTO> save(@RequestBody ProdutoSaveDTO productSaveDTO) {
		final Produto product = productSaveDTO.toEntity();
		final Produto savedProduct = this.produtoService.save(product);
		final ProdutoDTO savedProductDTO = ProdutoDTO.fromEntity(savedProduct);

		return ResponseEntity.ok(savedProductDTO);
	}

	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProdutoDTO> update(@RequestBody ProdutoSaveDTO productUpdateDTO, @PathVariable("id") Long id) {
		throw new RuntimeException("Not inplemented");
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProdutoDTO> findById(@PathVariable("id") Long id) {
		throw new RuntimeException("Not inplemented");
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProdutoDTO>> findAll() {
		throw new RuntimeException("Not inplemented");
	}

	@GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProdutoDTO>> findAllByFilters(
			@RequestParam("q") String nameOrDescription,
			@RequestParam("min_price") BigDecimal minPrice,
			@RequestParam("max_price") BigDecimal maxPrice) {

		throw new RuntimeException("Not inplemented");
	}

	@DeleteMapping(value = "/{id}")
	public void deleteById(@PathVariable("id") Long id) {
		throw new RuntimeException("Not inplemented");
	}

}
