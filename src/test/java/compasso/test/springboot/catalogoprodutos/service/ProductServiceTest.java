package compasso.test.springboot.catalogoprodutos.service;

import compasso.test.springboot.catalogoprodutos.exception.EntityNotFoundException;
import compasso.test.springboot.catalogoprodutos.model.Product;
import compasso.test.springboot.catalogoprodutos.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static compasso.test.springboot.catalogoprodutos.repository.specifications.ProductSpecifications.productWithMinMaxPrice;
import static compasso.test.springboot.catalogoprodutos.repository.specifications.ProductSpecifications.produtoWithNameOrDescription;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.jpa.domain.Specification.where;

@SpringBootTest
@ActiveProfiles("h2")
class ProductServiceTest {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@AfterEach
	void tearDown() {
		this.productRepository.deleteAll();
	}

	@Test
	void testProductShouldBeSaved() {
		final Product product = new Product("Desodorante", "Desodorante Azul", BigDecimal.valueOf(15.25));
		final Product savedProduct = this.productService.save(product);

		assertNotNull(savedProduct.getId());
		assertEquals(product.getName(), savedProduct.getName());
		assertEquals(product.getDescription(), savedProduct.getDescription());
		assertEquals(product.getPrice(), savedProduct.getPrice());
	}

	@Test
	void testProductShouldBeUpdated() {
		final Product product = new Product("Desodorante", "Desodorante Azul", BigDecimal.valueOf(15.25));

		final Product savedProduct = this.productService.save(product);

		final String newName = "Desodorante.";
		final String newDescription = "Desodorante Verde";
		final BigDecimal newValue = BigDecimal.valueOf(14L);

		savedProduct.setName(newName);
		savedProduct.setDescription(newDescription);
		savedProduct.setPrice(newValue);
		final Product updatedProduct = this.productService.update(savedProduct);

		assertEquals(newName, updatedProduct.getName());
		assertEquals(newDescription, updatedProduct.getDescription());
		assertEquals(newValue, updatedProduct.getPrice());

	}

	@Test
	void testUpdateProductShouldThrowException() {
		final Product product = new Product(1L, "Desodorante", "Desodorante Azul", BigDecimal.valueOf(15.25));


		final String exceptionMessage = String.format("Produto id %d inexistente.", product.getId());

		assertThrows(EntityNotFoundException.class, () -> {
			this.productService.update(product);
		}, exceptionMessage);
	}

	@Test
	void findAllShouldReturnSavedProducts() {
		final Product product = new Product("Desodorante", "Desodorante Azul", BigDecimal.valueOf(15.25));
		final Product product2 = new Product("Sabonete", "Sabonete top", BigDecimal.valueOf(12L));
		this.productService.save(product);
		this.productService.save(product2);

		final List<Product> products = this.productService.findAll();

		final int expectedProductsSize = 2;
		assertEquals(expectedProductsSize, products.size());
	}

	@Test
	void findAllShouldReturnEmptyList() {
		final List<Product> products = this.productService.findAll();

		final int expectedProductsSize = 0;
		assertEquals(expectedProductsSize, products.size());
	}

	@Test
	void testShouldFindByIdShouldReturnProduct() {
		final Product product = new Product("Desodorante", "Desodorante Azul", BigDecimal.valueOf(15.25));
		final Product savedProduct = this.productService.save(product);

		final Long expectedId = savedProduct.getId();
		final String expectedDescription = product.getDescription();
		final BigDecimal expectedPrice = product.getPrice();

		final Product foundProduct = this.productService.findById(expectedId);

		assertEquals(expectedId, foundProduct.getId());
		assertEquals(expectedDescription, foundProduct.getDescription());
		assertEquals(expectedPrice, foundProduct.getPrice());
	}

	@Test
	void testFindByIdShouldThrowException() {
		Long id = 1231324L;
		final String exceptionMessage = String.format("Produto id %d inexistente.", id);
		assertThrows(EntityNotFoundException.class, () -> {
			this.productService.findById(id);
		}, exceptionMessage);


	}

	@Test
	void testShouldDeleteById() {
		final Product product = new Product("Desodorante", "Desodorante Azul", BigDecimal.valueOf(15.25));
		final Product savedProduct = this.productService.save(product);

		final String exceptionMessage = String.format("Produto id %d inexistente.", savedProduct.getId());


		this.productService.delete(savedProduct.getId());
		assertThrows(EntityNotFoundException.class, () -> {
			this.productService.findById(product.getId());
		}, exceptionMessage);
	}

	@Test
	void testShouldFindProductByName() {
		final Product product = new Product("Desodorante", "Desodorante Azul", BigDecimal.valueOf(15.25));
		this.productService.save(product);
		final Product product2 = new Product("Sabonete", "Sabonete top", BigDecimal.valueOf(26L));
		this.productService.save(product2);


		final String nameOrDescriptionFilterValue = "Desodorante";
		List<Product> products = this.productService.findAllBySpecification(
				where(produtoWithNameOrDescription(nameOrDescriptionFilterValue)
						.and(productWithMinMaxPrice(null, null))));

		assertEquals(1, products.size());

		final Product foundProduct = products.stream().findFirst().get();
		assertEquals(product.getDescription(), foundProduct.getDescription());
	}

	@Test
	void testShouldFindProductByDescription() {
		final Product product = new Product("Desodorante", "Desodorante Azul", BigDecimal.valueOf(15.25));
		this.productService.save(product);
		final Product product2 = new Product("Sabonete", "Sabonete top", BigDecimal.valueOf(26L));
		this.productService.save(product2);


		final String nameOrDescriptionFilterValue = "Desodorante Azul";
		List<Product> products = this.productService.findAllBySpecification(
				where(produtoWithNameOrDescription(nameOrDescriptionFilterValue)
						.and(productWithMinMaxPrice(null, null))));

		assertEquals(1, products.size());

		final Product foundProduct = products.stream().findFirst().get();
		assertEquals(product.getDescription(), foundProduct.getDescription());
	}

	@Test
	void testShouldFindProductByPriceMin() {
		final Product product = new Product("Desodorante", "Desodorante Azul", BigDecimal.valueOf(15.25));
		this.productService.save(product);
		final Product product2 = new Product("Sabonete", "Sabonete top", BigDecimal.valueOf(26L));
		this.productService.save(product2);


		final BigDecimal minPriceFilterValue = BigDecimal.valueOf(26L);
		List<Product> products = this.productService.findAllBySpecification(
				where(produtoWithNameOrDescription(null)
						.and(productWithMinMaxPrice(minPriceFilterValue, null))));

		assertEquals(1, products.size());

		final Product foundProduct = products.stream().findFirst().get();
		assertEquals(product2.getDescription(), foundProduct.getDescription());
	}

	@Test
	void testShouldFindProductByPriceMax() {
		final Product product = new Product("Desodorante", "Desodorante Azul", BigDecimal.valueOf(15.25));
		this.productService.save(product);
		final Product product2 = new Product("Sabonete", "Sabonete top", BigDecimal.valueOf(26L));
		this.productService.save(product2);


		final BigDecimal maxPriceFilterValue = BigDecimal.valueOf(15.25);
		List<Product> products = this.productService.findAllBySpecification(
				where(produtoWithNameOrDescription(null)
						.and(productWithMinMaxPrice(null, maxPriceFilterValue))));

		assertEquals(1, products.size());

		final Product foundProduct = products.stream().findFirst().get();
		assertEquals(product.getDescription(), foundProduct.getDescription());
	}

	@Test
	void testShouldFindProductByPriceMinMax() {
		final Product product = new Product("Desodorante", "Desodorante Azul", BigDecimal.valueOf(15.25));
		this.productService.save(product);
		final Product product2 = new Product("Sabonete", "Sabonete top", BigDecimal.valueOf(26L));
		this.productService.save(product2);
		final Product product3 = new Product("Pão", "Pão Francês", BigDecimal.valueOf(2L));
		this.productService.save(product3);


		List<Product> products = this.productService.findAllBySpecification(
				where(produtoWithNameOrDescription(null)
						.and(productWithMinMaxPrice(BigDecimal.valueOf(2L), BigDecimal.valueOf(16L)))));

		assertEquals(2, products.size());

		final boolean productsHasSabonete = products.stream().anyMatch(filteredProduct -> filteredProduct.getDescription().equals(product2.getDescription()));

		assertFalse(productsHasSabonete);


	}
}
