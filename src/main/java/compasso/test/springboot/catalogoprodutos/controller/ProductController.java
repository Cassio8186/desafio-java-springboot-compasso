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

import static compasso.test.springboot.catalogoprodutos.repository.specifications.ProductSpecifications.productWithMinMaxPrice;
import static compasso.test.springboot.catalogoprodutos.repository.specifications.ProductSpecifications.produtoWithNameOrDescription;
import static org.springframework.data.jpa.domain.Specification.where;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

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
		final List<Product> products = this.productService.findAll();
		final List<ProductDTO> productDTOS = ProductDTO.fromEntity(products);

		return ResponseEntity.ok(productDTOS);
	}

	@GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProductDTO>> findAllByFilters(
			@RequestParam(value = "q", required = false) String nameOrDescription,
			@RequestParam(value = "min_price", required = false) BigDecimal minPrice,
			@RequestParam(value = "max_price", required = false) BigDecimal maxPrice) {

		final List<Product> products = this.productService.findAllBySpecification(where(produtoWithNameOrDescription(nameOrDescription)).
				and(productWithMinMaxPrice(minPrice, maxPrice)));

		List<ProductDTO> productDTOs = ProductDTO.fromEntity(products);

		return ResponseEntity.ok(productDTOs);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
		try {
			this.productService.deleteById(id);
			return new ResponseEntity<>(OK);
		} catch (EntityNotFoundException ex) {
			return new ResponseEntity<>(NOT_FOUND);
		}

	}

}
