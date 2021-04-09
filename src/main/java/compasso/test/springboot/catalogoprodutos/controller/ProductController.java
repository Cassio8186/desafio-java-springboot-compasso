package compasso.test.springboot.catalogoprodutos.controller;

import compasso.test.springboot.catalogoprodutos.exception.EntityNotFoundException;
import compasso.test.springboot.catalogoprodutos.model.Product;
import compasso.test.springboot.catalogoprodutos.model.dto.ProductDTO;
import compasso.test.springboot.catalogoprodutos.model.dto.ProductSaveDTO;
import compasso.test.springboot.catalogoprodutos.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/products")
public class ProductController {
	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductDTO> save(@Valid @RequestBody ProductSaveDTO productSaveDTO) {
		final Product product = productSaveDTO.toEntity();
		final Product savedProduct = this.productService.save(product);
		final ProductDTO savedProductDTO = ProductDTO.fromEntity(savedProduct);

		return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
	}

	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductDTO> update(@Valid @RequestBody ProductSaveDTO productUpdateDTO, @PathVariable("id") Long id) {
		final Product product = productUpdateDTO.toEntity();
		product.setId(id);

		final Product updatedProduct = this.productService.update(product);
		final ProductDTO productDTO = ProductDTO.fromEntity(updatedProduct);

		return ResponseEntity.ok(productDTO);

	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductDTO> findById(@PathVariable("id") Long id) {
		try {
			final Product product = this.productService.findById(id);

			return ResponseEntity.ok(ProductDTO.fromEntity(product));

		} catch (EntityNotFoundException ex) {
			return new ResponseEntity<>(NOT_FOUND);
		}
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProductDTO>> findAll() {
		throw new RuntimeException("Not inplemented");
	}

	@GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProductDTO>> findAllByFilters(
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
