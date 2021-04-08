package compasso.test.springboot.catalogoprodutos.controller;

import com.google.gson.Gson;
import compasso.test.springboot.catalogoprodutos.model.Product;
import compasso.test.springboot.catalogoprodutos.model.dto.ProductSaveDTO;
import compasso.test.springboot.catalogoprodutos.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProductRepository productRepository;

	@BeforeEach
	void beforeEach() {
		this.productRepository.deleteAll();
	}

	@Test
	void saveShouldReturnSavedProduct() throws Exception {
		final ProductSaveDTO product = new ProductSaveDTO("Desodorante", "Desodorante Verde", BigDecimal.valueOf(20L));
		final Gson gson = new Gson();
		final String json = gson.toJson(product);

		final String url = "/products";
		this.mockMvc.perform(post(url)
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
	void saveShouldReturnUpdateProduct() throws Exception {
		final Product product = new Product("Desodorante", "Desodorante Verde", BigDecimal.valueOf(20L));
		final Product savedProduct = this.productRepository.save(product);

		final ProductSaveDTO productSaveDTO = ProductSaveDTO.fromEntity(product);


		productSaveDTO.setName("Desodor");
		productSaveDTO.setDescription("Desodorante Azul");
		productSaveDTO.setPrice(BigDecimal.ONE);

		final Gson gson = new Gson();
		final String json = gson.toJson(productSaveDTO);

		final String url = String.format("/products/%d", savedProduct.getId());

		this.mockMvc.perform(put(url)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(json))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value(productSaveDTO.getName()))
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.description").value(productSaveDTO.getDescription()))
				.andExpect(jsonPath("$.price").value(productSaveDTO.getPrice()));
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
