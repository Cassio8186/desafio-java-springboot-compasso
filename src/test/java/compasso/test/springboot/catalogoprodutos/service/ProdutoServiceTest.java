package compasso.test.springboot.catalogoprodutos.service;

import compasso.test.springboot.catalogoprodutos.exception.EntityNotFoundException;
import compasso.test.springboot.catalogoprodutos.model.Produto;
import compasso.test.springboot.catalogoprodutos.repository.ProdutoRepository;
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
class ProdutoServiceTest {

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private ProdutoRepository produtoRepository;

	@AfterEach
	void tearDown() {
		this.produtoRepository.deleteAll();
	}

	@Test
	void testProductShouldBeSaved() {
		final Produto product = new Produto("Desodorante", "Desodorante Azul", BigDecimal.valueOf(15.25));
		final Produto savedProduct = this.produtoService.save(product);

		assertNotNull(savedProduct.getId());
		assertEquals(product.getName(), savedProduct.getName());
		assertEquals(product.getDescription(), savedProduct.getDescription());
		assertEquals(product.getPrice(), savedProduct.getPrice());
	}

	@Test
	void testProductShouldBeUpdated() {
		final Produto product = new Produto("Desodorante", "Desodorante Azul", BigDecimal.valueOf(15.25));

		final Produto savedProduct = this.produtoService.save(product);

		final String newName = "Desodorante.";
		final String newDescription = "Desodorante Verde";
		final BigDecimal newValue = BigDecimal.valueOf(14L);

		savedProduct.setName(newName);
		savedProduct.setDescription(newDescription);
		savedProduct.setPrice(newValue);
		final Produto updatedProduct = this.produtoService.update(savedProduct);

		assertEquals(newName, updatedProduct.getName());
		assertEquals(newDescription, updatedProduct.getDescription());
		assertEquals(newValue, updatedProduct.getPrice());

	}

	@Test
	void testUpdateProductShouldThrowException() {
		final Produto product = new Produto(1L, "Desodorante", "Desodorante Azul", BigDecimal.valueOf(15.25));


		final String exceptionMessage = String.format("Produto id %d inexistente.", product.getId());

		assertThrows(EntityNotFoundException.class, () -> {
			this.produtoService.update(product);
		}, exceptionMessage);
	}

	@Test
	void findAllShouldReturnSavedProducts() {
		final Produto product = new Produto("Desodorante", "Desodorante Azul", BigDecimal.valueOf(15.25));
		final Produto product2 = new Produto("Sabonete", "Sabonete top", BigDecimal.valueOf(12L));
		this.produtoService.save(product);
		this.produtoService.save(product2);

		final List<Produto> products = this.produtoService.findAll();

		final int expectedProductsSize = 2;
		assertEquals(expectedProductsSize, products.size());
	}

	@Test
	void findAllShouldReturnEmptyList() {
		final List<Produto> products = this.produtoService.findAll();

		final int expectedProductsSize = 0;
		assertEquals(expectedProductsSize, products.size());
	}

	@Test
	void testShouldFindByIdShouldReturnProduct() {
		final Produto product = new Produto("Desodorante", "Desodorante Azul", BigDecimal.valueOf(15.25));
		final Produto savedProduct = this.produtoService.save(product);

		final Long expectedId = savedProduct.getId();
		final String expectedDescription = product.getDescription();
		final BigDecimal expectedPrice = product.getPrice();

		final Produto foundProduct = this.produtoService.findById(expectedId);

		assertEquals(expectedId, foundProduct.getId());
		assertEquals(expectedDescription, foundProduct.getDescription());
		assertEquals(expectedPrice, foundProduct.getPrice());
	}

	@Test
	void testFindByIdShouldThrowException() {
		Long id = 1231324L;
		final String exceptionMessage = String.format("Produto id %d inexistente.", id);
		assertThrows(EntityNotFoundException.class, () -> {
			this.produtoService.findById(id);
		}, exceptionMessage);


	}

	@Test
	void testShouldDeleteById() {
		final Produto product = new Produto("Desodorante", "Desodorante Azul", BigDecimal.valueOf(15.25));
		final Produto savedProduct = this.produtoService.save(product);

		final String exceptionMessage = String.format("Produto id %d inexistente.", savedProduct.getId());


		this.produtoService.delete(savedProduct.getId());
		assertThrows(EntityNotFoundException.class, () -> {
			this.produtoService.findById(product.getId());
		}, exceptionMessage);
	}

	@Test
	void testShouldFindProductByName() {
		final Produto product = new Produto("Desodorante", "Desodorante Azul", BigDecimal.valueOf(15.25));
		this.produtoService.save(product);
		final Produto product2 = new Produto("Sabonete", "Sabonete top", BigDecimal.valueOf(26L));
		this.produtoService.save(product2);


		final String nameOrDescriptionFilterValue = "Desodorante";
		List<Produto> products = this.produtoService.findAllBySpecification(
				where(produtoWithNameOrDescription(nameOrDescriptionFilterValue)
						.and(productWithMinMaxPrice(null, null))));

		assertEquals(1, products.size());

		final Produto foundProduct = products.stream().findFirst().get();
		assertEquals(product.getDescription(), foundProduct.getDescription());
	}

	@Test
	void testShouldFindProductByDescription() {
		final Produto product = new Produto("Desodorante", "Desodorante Azul", BigDecimal.valueOf(15.25));
		this.produtoService.save(product);
		final Produto product2 = new Produto("Sabonete", "Sabonete top", BigDecimal.valueOf(26L));
		this.produtoService.save(product2);


		final String nameOrDescriptionFilterValue = "Desodorante Azul";
		List<Produto> products = this.produtoService.findAllBySpecification(
				where(produtoWithNameOrDescription(nameOrDescriptionFilterValue)
						.and(productWithMinMaxPrice(null, null))));

		assertEquals(1, products.size());

		final Produto foundProduct = products.stream().findFirst().get();
		assertEquals(product.getDescription(), foundProduct.getDescription());
	}

	@Test
	void testShouldFindProductByPriceMin() {
		final Produto product = new Produto("Desodorante", "Desodorante Azul", BigDecimal.valueOf(15.25));
		this.produtoService.save(product);
		final Produto product2 = new Produto("Sabonete", "Sabonete top", BigDecimal.valueOf(26L));
		this.produtoService.save(product2);


		final BigDecimal minPriceFilterValue = BigDecimal.valueOf(26L);
		List<Produto> products = this.produtoService.findAllBySpecification(
				where(produtoWithNameOrDescription(null)
						.and(productWithMinMaxPrice(minPriceFilterValue, null))));

		assertEquals(1, products.size());

		final Produto foundProduct = products.stream().findFirst().get();
		assertEquals(product2.getDescription(), foundProduct.getDescription());
	}

	@Test
	void testShouldFindProductByPriceMax() {
		final Produto product = new Produto("Desodorante", "Desodorante Azul", BigDecimal.valueOf(15.25));
		this.produtoService.save(product);
		final Produto product2 = new Produto("Sabonete", "Sabonete top", BigDecimal.valueOf(26L));
		this.produtoService.save(product2);


		final BigDecimal maxPriceFilterValue = BigDecimal.valueOf(15.25);
		List<Produto> products = this.produtoService.findAllBySpecification(
				where(produtoWithNameOrDescription(null)
						.and(productWithMinMaxPrice(null, maxPriceFilterValue))));

		assertEquals(1, products.size());

		final Produto foundProduct = products.stream().findFirst().get();
		assertEquals(product.getDescription(), foundProduct.getDescription());
	}

	@Test
	void testShouldFindProductByPriceMinMax() {
		final Produto product = new Produto("Desodorante", "Desodorante Azul", BigDecimal.valueOf(15.25));
		this.produtoService.save(product);
		final Produto product2 = new Produto("Sabonete", "Sabonete top", BigDecimal.valueOf(26L));
		this.produtoService.save(product2);
		final Produto product3 = new Produto("Pão", "Pão Francês", BigDecimal.valueOf(2L));
		this.produtoService.save(product3);


		List<Produto> products = this.produtoService.findAllBySpecification(
				where(produtoWithNameOrDescription(null)
						.and(productWithMinMaxPrice(BigDecimal.valueOf(2L), BigDecimal.valueOf(16L)))));

		assertEquals(2, products.size());

		final boolean productsHasSabonete = products.stream().anyMatch(filteredProduct -> filteredProduct.getDescription().equals(product2.getDescription()));

		assertFalse(productsHasSabonete);


	}
}
