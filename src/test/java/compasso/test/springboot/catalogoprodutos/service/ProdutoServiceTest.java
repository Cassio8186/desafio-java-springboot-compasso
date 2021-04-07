package compasso.test.springboot.catalogoprodutos.service;

import compasso.test.springboot.catalogoprodutos.model.Produto;
import compasso.test.springboot.catalogoprodutos.repository.ProdutoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

		assertThrows(RuntimeException.class, () -> {
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
		assertThrows(RuntimeException.class, () -> {
			this.produtoService.findById(id);
		}, exceptionMessage);


	}

	@Test
	void testShouldDeleteById() {
		final Produto product = new Produto("Desodorante", "Desodorante Azul", BigDecimal.valueOf(15.25));
		final Produto savedProduct = this.produtoService.save(product);

		final String exceptionMessage = String.format("Produto id %d inexistente.", savedProduct.getId());


		this.produtoService.delete(savedProduct.getId());
		assertThrows(RuntimeException.class, () -> {
			this.produtoService.findById(product.getId());
		}, exceptionMessage);
	}
}
