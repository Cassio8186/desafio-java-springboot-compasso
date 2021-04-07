package compasso.test.springboot.catalogoprodutos.config.repository;

import compasso.test.springboot.catalogoprodutos.config.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}
