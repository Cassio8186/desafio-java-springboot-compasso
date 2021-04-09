package compasso.test.springboot.catalogoprodutos.controller;

import compasso.test.springboot.catalogoprodutos.exception.EntityNotFoundException;
import compasso.test.springboot.catalogoprodutos.model.Product;
import compasso.test.springboot.catalogoprodutos.model.dto.ProductDTO;
import compasso.test.springboot.catalogoprodutos.model.dto.ProductSaveDTO;
import compasso.test.springboot.catalogoprodutos.service.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger log = LoggerFactory.getLogger(ProductController.class);
	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Salva um produto")
	public ResponseEntity<ProductDTO> save(@Valid @RequestBody ProductSaveDTO productSaveDTO) {
		try {
			log.info("Save product request: [{}]", productSaveDTO);
			final Product product = productSaveDTO.toEntity();
			final Product savedProduct = this.productService.save(product);
			final ProductDTO savedProductDTO = ProductDTO.fromEntity(savedProduct);

			log.info("Save product request was successful: [{}]", savedProduct);

			return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error("Save product request failed: [{}]", productSaveDTO);
			throw e;
		}
	}

	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Atualiza um produto")
	public ResponseEntity<ProductDTO> update(@Valid @RequestBody ProductSaveDTO productUpdateDTO, @PathVariable("id") Long id) {
		try {
			log.info("Update product request: [{}]", productUpdateDTO);

			final Product product = productUpdateDTO.toEntity();
			product.setId(id);

			final Product updatedProduct = this.productService.update(product);
			final ProductDTO productDTO = ProductDTO.fromEntity(updatedProduct);

			log.info("Update product request was successful: [{}]", productDTO);

			return ResponseEntity.ok(productDTO);
		} catch (Exception e) {
			log.error("Update product request failed: [{}]", productUpdateDTO);
			throw e;
		}

	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna um produto por id")
	@ApiResponses({@ApiResponse(code = 404, message = "Not Found")})
	public ResponseEntity<ProductDTO> findById(@PathVariable("id") Long id) {
		try {
			log.info("Find product by id {} request", id);

			final Product product = this.productService.findById(id);

			log.info("Find product by id {} request was successful", id);

			return ResponseEntity.ok(ProductDTO.fromEntity(product));
		} catch (EntityNotFoundException ex) {
			log.error("Product by id {} was not found", id);

			return new ResponseEntity<>(NOT_FOUND);
		}		catch (Exception ex){
			log.info("Find product by id {} request failed",id);
			throw ex;
		}
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna uma lista com todos os produtos")
	public ResponseEntity<List<ProductDTO>> findAll() {
		log.info("Find all products request");
		final List<Product> products = this.productService.findAll();
		final List<ProductDTO> productDTOS = ProductDTO.fromEntity(products);

		log.info("Find all products was successful");

		return ResponseEntity.ok(productDTOS);
	}

	@GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna uma lista de produtos filtrados")
	public ResponseEntity<List<ProductDTO>> findAllByFilters(
			@RequestParam(value = "q", required = false) String nameOrDescription,
			@RequestParam(value = "min_price", required = false) BigDecimal minPrice,
			@RequestParam(value = "max_price", required = false) BigDecimal maxPrice) {
		log.info("Find all by filter products request: [q: {},minPrice: {},maxPrice: {}]",
				nameOrDescription,
				minPrice,
				maxPrice);

		final List<Product> products = this.productService.findAllBySpecification(where(produtoWithNameOrDescription(nameOrDescription)).
				and(productWithMinMaxPrice(minPrice, maxPrice)));

		List<ProductDTO> productDTOs = ProductDTO.fromEntity(products);

		log.info("Find all by filter products was successfull: [q: {},minPrice: {},maxPrice: {}]",
				nameOrDescription,
				minPrice,
				maxPrice);

		return ResponseEntity.ok(productDTOs);
	}

	@DeleteMapping(value = "/{id}")
	@ApiOperation(value = "Remove um produto")
	public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
		try {
			log.info("Delete product by id {} request",id);
			this.productService.deleteById(id);
			log.info("Delete product by id {} request was successful",id);
			return new ResponseEntity<>(OK);
		} catch (EntityNotFoundException ex) {
			log.info("Product by id {} was not found",id);
			return new ResponseEntity<>(NOT_FOUND);
		} catch (Exception ex) {
			log.info("Delete product by id {} request failed", id);
			throw ex;
		}

	}

}
