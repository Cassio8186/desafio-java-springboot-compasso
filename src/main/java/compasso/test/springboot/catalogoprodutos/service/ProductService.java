package compasso.test.springboot.catalogoprodutos.service;

import compasso.test.springboot.catalogoprodutos.exception.EntityNotFoundException;
import compasso.test.springboot.catalogoprodutos.model.Product;
import compasso.test.springboot.catalogoprodutos.repository.ProductRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@Transactional
public class ProductService {
	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Product save(Product product) {
		return this.productRepository.save(product);
	}

	public Product update(Product product) {
		final Optional<Product> optionalProduct = this.productRepository.findById(product.getId());

		final boolean productDoesNotExists = !optionalProduct.isPresent();
		if (productDoesNotExists) {
			throw entityNotFoundExceptionSupplierById(product.getId()).get();
		}
		return this.productRepository.save(product);

	}

	public Supplier<EntityNotFoundException> entityNotFoundExceptionSupplierById(Long id) {
		return () -> {
			final String exceptionMessage = String.format("Produto id %d inexistente.", id);
			return new EntityNotFoundException(exceptionMessage);
		};
	}

	public Supplier<EntityNotFoundException> entityNotFoundExceptionSupplierById(String id) {
		return () -> {
			final String exceptionMessage = String.format("Produto id %s inexistente.", id);
			return new EntityNotFoundException(exceptionMessage);
		};
	}

	public List<Product> findAll() {
		return this.productRepository.findAll();
	}

	public Product findById(Long id) {
		final Optional<Product> optionalProduct = this.productRepository.findById(id);
		return optionalProduct.orElseThrow(entityNotFoundExceptionSupplierById(id));
	}

	public void deleteById(Long id) {
		final Optional<Product> optionalProduct = this.productRepository.findById(id);
		final boolean productDoesNotExists = !optionalProduct.isPresent();

		if (productDoesNotExists) {
			throw entityNotFoundExceptionSupplierById(id).get();
		}

		final Product product = optionalProduct.get();
		this.productRepository.delete(product);
	}

	public List<Product> findAllBySpecification(Specification<Product> specification) {
		return this.productRepository.findAll(specification);
	}
}
