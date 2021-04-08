package compasso.test.springboot.catalogoprodutos.controller;

import com.google.gson.Gson;
import compasso.test.springboot.catalogoprodutos.model.Produto;
import compasso.test.springboot.catalogoprodutos.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProdutoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProdutoRepository produtoRepository;

	@BeforeEach
	void beforeEach() {
		this.produtoRepository.deleteAll();
	}

	@Test
	void saveShouldReturnSavedProduct() throws Exception {
		final Produto product = new Produto("Desodorante", "Desodorante Verde", BigDecimal.valueOf(20L));
		final Gson gson = new Gson();
		final String json = gson.toJson(product);

		this.mockMvc.perform(post("/products")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(json))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value(product.getName()))
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.description").value(product.getDescription()))
				.andExpect(jsonPath("$.price").value(product.getPrice()));
	}


	@Test
	void update() {
	}

	@Test
	void findById() {
	}

	@Test
	void findAll() {
	}

	@Test
	void findAllByFilters() {
	}

	@Test
	void deleteById() {
	}
}
