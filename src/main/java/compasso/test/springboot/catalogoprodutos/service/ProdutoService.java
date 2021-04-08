package compasso.test.springboot.catalogoprodutos.service;

import compasso.test.springboot.catalogoprodutos.exception.EntityNotFoundException;
import compasso.test.springboot.catalogoprodutos.model.Produto;
import compasso.test.springboot.catalogoprodutos.repository.ProdutoRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@Transactional
public class ProdutoService {
	private final ProdutoRepository produtoRepository;

	public ProdutoService(ProdutoRepository produtoRepository) {
		this.produtoRepository = produtoRepository;
	}

	public Produto save(Produto product) {
		return this.produtoRepository.save(product);
	}

	public Produto update(Produto product) {
		final Optional<Produto> optionalProduct = this.produtoRepository.findById(product.getId());

		final boolean productDoesNotExists = !optionalProduct.isPresent();
		if (productDoesNotExists) {
			throw entityNotFoundExceptionSupplierById(product.getId()).get();
		}
		return this.produtoRepository.save(product);

	}

	private Supplier<EntityNotFoundException> entityNotFoundExceptionSupplierById(Long id) {
		return () -> {
			final String exceptionMessage = String.format("Produto id %d inexistente.", id);
			return new EntityNotFoundException(exceptionMessage);
		};
	}

	public List<Produto> findAll() {
		return this.produtoRepository.findAll();
	}

	public Produto findById(Long id) {
		final Optional<Produto> optionalProduct = this.produtoRepository.findById(id);
		return optionalProduct.orElseThrow(entityNotFoundExceptionSupplierById(id));
	}

	public void delete(Long id) {
		final Optional<Produto> optionalProduct = this.produtoRepository.findById(id);
		final boolean productDoesNotExists = !optionalProduct.isPresent();

		if (productDoesNotExists) {
			throw entityNotFoundExceptionSupplierById(id).get();
		}

		final Produto product = optionalProduct.get();
		this.produtoRepository.delete(product);
	}

	public List<Produto> findAllBySpecification(Specification<Produto> specification) {
		return this.produtoRepository.findAll(specification);
	}
}
