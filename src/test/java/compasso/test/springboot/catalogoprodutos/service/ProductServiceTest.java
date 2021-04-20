package compasso.test.springboot.catalogoprodutos.service;

import com.github.javafaker.Faker;
import compasso.test.springboot.catalogoprodutos.exception.EntityNotFoundException;
import compasso.test.springboot.catalogoprodutos.model.Product;
import compasso.test.springboot.catalogoprodutos.repository.ProductRepository;
import compasso.test.springboot.catalogoprodutos.util.ProductUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.*;

import static compasso.test.springboot.catalogoprodutos.repository.specifications.ProductSpecifications.productWithMinMaxPrice;
import static compasso.test.springboot.catalogoprodutos.repository.specifications.ProductSpecifications.produtoWithNameOrDescription;
import static compasso.test.springboot.catalogoprodutos.util.ProductUtil.cloneProduct;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.data.jpa.domain.Specification.where;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	public static final Faker faker = Faker.instance();

	@InjectMocks
	private ProductService productService;

	@Mock
	private ProductRepository mockProductRepository;

	@Test
	void testProductShouldBeSaved() {
		final Product product = new Product("Desodorante", "Desodorante Azul", BigDecimal.valueOf(15.25));

		final Product mockedProduct = cloneProduct(product);
		final long mockedId = 5L;
		mockedProduct.setId(mockedId);

		when(mockProductRepository.save(any(Product.class))).thenReturn(mockedProduct);

		final Product savedProduct = this.productService.save(product);

		assertEquals(mockedProduct.getId(), mockedId);
		assertEquals(product.getName(), savedProduct.getName());
		assertEquals(product.getDescription(), savedProduct.getDescription());
		assertEquals(product.getPrice(), savedProduct.getPrice());
	}

	@Test
	void testProductShouldBeUpdated() {
		final long productId = 15L;
		final Product product = ProductUtil.generateProduct(false);
		when(mockProductRepository.findById(productId)).thenReturn(Optional.of(product));
		final String newName = "Desodorante.";
		final String newDescription = "Desodorante Verde";
		final BigDecimal newValue = BigDecimal.valueOf(14L);

		final Product updateProduct = new Product(15L, newName, newDescription, newValue);
		when(mockProductRepository.save(updateProduct)).thenReturn(updateProduct);


		final Product updatedProduct = this.productService.update(updateProduct);

		assertEquals(newName, updatedProduct.getName());
		assertEquals(newDescription, updatedProduct.getDescription());
		assertEquals(newValue, updatedProduct.getPrice());

	}

	@Test
	void testUpdateProductShouldThrowException() {
		when(mockProductRepository.findById(anyLong())).thenReturn(Optional.empty());

		final Product product = ProductUtil.generateProduct(true);

		final String exceptionMessage = String.format("Produto id %d inexistente.", product.getId());

		assertThrows(EntityNotFoundException.class, () -> {
			this.productService.update(product);
		}, exceptionMessage);
	}

	@Test
	void findAllShouldReturnSavedProducts() {
		final Product product = ProductUtil.generateProduct(false);
		final Product product2 = ProductUtil.generateProduct(false);

		when(mockProductRepository.findAll()).thenReturn(Arrays.asList(product, product2));

		final List<Product> products = this.productService.findAll();

		final int expectedProductsSize = 2;
		assertEquals(expectedProductsSize, products.size());
	}

	@Test
	void findAllShouldReturnEmptyList() {
		when(mockProductRepository.findAll()).thenReturn(new ArrayList<>());

		final List<Product> products = this.productService.findAll();

		final int expectedProductsSize = 0;
		assertEquals(expectedProductsSize, products.size());
	}

	@Test
	void testShouldFindByIdShouldReturnProduct() {
		final Product product = ProductUtil.generateProduct(true);

		final Faker faker = ProductServiceTest.faker;
		final long randomId = faker.number().randomNumber();
		when(mockProductRepository.findById(randomId)).thenReturn(Optional.of(product));

		final Product foundProduct = this.productService.findById(randomId);

		assertEquals(product.getId(), foundProduct.getId());
		assertEquals(product.getDescription(), foundProduct.getDescription());
		assertEquals(product.getPrice(), foundProduct.getPrice());
		assertEquals(product.getName(), foundProduct.getName());
	}

	@Test
	void testFindByIdShouldThrowException() {
		Long id = faker.number().randomNumber();
		when(mockProductRepository.findById(id)).thenReturn(Optional.empty());

		final String exceptionMessage = String.format("Produto id %d inexistente.", id);
		assertThrows(EntityNotFoundException.class, () -> {
			this.productService.findById(id);
		}, exceptionMessage);


	}

	@Test
	void testShouldDeleteById() {
		final Long id = faker.number().randomNumber();
		final Product product = new Product();
		product.setId(id);
		when(mockProductRepository.findById(id)).thenReturn(Optional.of(product));

		this.productService.deleteById(id);
		verify(mockProductRepository, times(1)).delete(product);
	}

	@Test
	void testShouldFindProductBySpecification() {
		final Product product = ProductUtil.generateProduct(true);

		final String nameOrDescriptionFilterValue = product.getDescription();
		final Specification<Product> productSpecification = where(produtoWithNameOrDescription(nameOrDescriptionFilterValue)
				.and(productWithMinMaxPrice(BigDecimal.valueOf(25L), BigDecimal.valueOf(30L))));
		when(mockProductRepository.findAll(productSpecification)).thenReturn(Collections.singletonList(product));

		List<Product> products = this.productService.findAllBySpecification(productSpecification);

		assertEquals(1, products.size());

		final Product foundProduct = products.stream().findFirst().get();
		assertEquals(product.getDescription(), foundProduct.getDescription());
	}

	@Test
	void entityNotFoundExceptionSupplierById() {
		final Long id = faker.number().randomNumber();
		final String actualMessage = this.productService.entityNotFoundExceptionSupplierById(id).get().getMessage();

		String expectedMessage = String.format("Produto id %d inexistente.", id);
		assertEquals(expectedMessage, actualMessage);

	}

	@Test
	void testEntityNotFoundExceptionSupplierById() {
		final String id = UUID.randomUUID().toString();
		final String actualMessage = this.productService.entityNotFoundExceptionSupplierById(id).get().getMessage();

		String expectedMessage = String.format("Produto id %s inexistente.", id);
		assertEquals(expectedMessage, actualMessage);
	}
}
