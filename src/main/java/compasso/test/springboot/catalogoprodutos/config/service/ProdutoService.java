package compasso.test.springboot.catalogoprodutos.config.service;

import compasso.test.springboot.catalogoprodutos.config.model.Produto;
import compasso.test.springboot.catalogoprodutos.config.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {
	private final ProdutoRepository produtoRepository;

	public ProdutoService(ProdutoRepository produtoRepository) {
		this.produtoRepository = produtoRepository;
	}

	public Produto save(Produto produto) {
		return null;
	}

	public Produto update(Produto produto) {
		return null;

	}

	public List<Produto> findAll() {
		return null;

	}

	public Produto findById(Long id) {
		return null;
	}

	public List<Produto> delete(Long id) {
		return null;
	}
}
