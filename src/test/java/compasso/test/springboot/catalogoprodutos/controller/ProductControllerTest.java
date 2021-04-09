package compasso.test.springboot.catalogoprodutos.controller;

import com.google.gson.Gson;
import compasso.test.springboot.catalogoprodutos.model.Product;
import compasso.test.springboot.catalogoprodutos.model.dto.ProductSaveDTO;
import compasso.test.springboot.catalogoprodutos.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

	private final Gson gson = new Gson();
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
		final String json = gson.toJson(product);

		final String url = "/products";
		this.mockMvc.perform(post(url)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(json))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value(product.getName()))
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.description").value(product.getDescription()))
				.andExpect(jsonPath("$.price").value(product.getPrice()));
	}

	@Test
	void saveShouldReturnValidationException() throws Exception {
		ProductSaveDTO product = new ProductSaveDTO(null, "Desodorante Verde", BigDecimal.valueOf(20L));
		String json = gson.toJson(product);

		final String url = "/products";
		this.mockMvc.perform(post(url)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(json))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(expectStatusCode(BAD_REQUEST))
				.andExpect(expectedExceptionMessage("name: deve ser preenchido."));

		product = new ProductSaveDTO("Something", null, BigDecimal.ONE);
		json = gson.toJson(product);

		this.mockMvc.perform(post(url)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(json))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(expectStatusCode(BAD_REQUEST))
				.andExpect(expectedExceptionMessage("description: deve ser preenchido."));
		;

		product = new ProductSaveDTO("Something", "Something else", null);
		json = gson.toJson(product);

		this.mockMvc.perform(post(url)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(json))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(expectStatusCode(BAD_REQUEST))
				.andExpect(expectedExceptionMessage("price: deve ser preenchido."));
		;

	}

	@Test
	void updateShouldReturnValidationException() throws Exception {
		ProductSaveDTO product = new ProductSaveDTO(null, "Desodorante Verde", BigDecimal.valueOf(20L));
		String json = gson.toJson(product);

		final String url = "/products/1";
		this.mockMvc.perform(put(url)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(json))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(expectStatusCode(BAD_REQUEST))
				.andExpect(expectedExceptionMessage("name: deve ser preenchido."));

		product = new ProductSaveDTO("Something", null, BigDecimal.ONE);
		json = gson.toJson(product);

		this.mockMvc.perform(put(url)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(json))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(expectStatusCode(BAD_REQUEST))
				.andExpect(expectedExceptionMessage("description: deve ser preenchido."));
		;

		product = new ProductSaveDTO("Something", "Something else", null);
		json = gson.toJson(product);

		this.mockMvc.perform(put(url)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(json))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(expectStatusCode(BAD_REQUEST))
				.andExpect(expectedExceptionMessage("price: deve ser preenchido."));
		;

	}

	@Test
	void updateShouldReturnUpdatedProduct() throws Exception {
		final Product product = new Product("Desodorante", "Desodorante Verde", BigDecimal.valueOf(20L));
		final Product savedProduct = this.productRepository.save(product);

		final ProductSaveDTO productSaveDTO = ProductSaveDTO.fromEntity(product);


		productSaveDTO.setName("Desodor");
		productSaveDTO.setDescription("Desodorante Azul");
		productSaveDTO.setPrice(BigDecimal.ONE);

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
	void updateShouldReturnNotFoundException() throws Exception {
		final ProductSaveDTO product = new ProductSaveDTO("Desodorante", "Desodorante Verde", BigDecimal.valueOf(20L));

		final String json = gson.toJson(product);

		final int expectedId = 2;
		final String url = String.format("/products/%d", expectedId);

		final String expectedExceptionMessage = String.format("Produto id %d inexistente.", expectedId);


		this.mockMvc.perform(put(url)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(json))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(expectStatusCode(NOT_FOUND))
				.andExpect(expectedExceptionMessage(expectedExceptionMessage));
	}

	private ResultMatcher expectedExceptionMessage(String expectedExceptionMessage) {
		return jsonPath("$.message").value(expectedExceptionMessage);
	}

	private ResultMatcher expectStatusCode(HttpStatus httpStatus) {
		return jsonPath("$.status_code").value(httpStatus.value());
	}


	@Test
	void findByIdShouldReturnProduct() throws Exception {
		final Product product = new Product("Desodorante", "DesodoranteVerde", BigDecimal.valueOf(14L));
		final Product saveProduct = this.productRepository.save(product);

		final String url = "/products/" + saveProduct.getId();
		this.mockMvc.perform(get(url))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value(product.getName()))
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.description").value(product.getDescription()))
				.andExpect(jsonPath("$.price").value(product.getPrice().doubleValue()));
	}

	@Test
	void findByIdShouldReturnNotFoundException() throws Exception {
		final String url = "/products/1241";

		this.mockMvc.perform(get(url))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

	@Test
	void testFindByIdShouldProductList() throws Exception {
		this.productRepository.save(new Product("a", "", BigDecimal.ZERO));
		this.productRepository.save(new Product("a", "", BigDecimal.ZERO));
		this.productRepository.save(new Product("a", "", BigDecimal.ZERO));


		final String url = "/products";

		this.mockMvc.perform(get(url))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", hasSize(3)));
	}

	@Test
	void testFindByIdShouldEmptyList() throws Exception {
		final String url = "/products";

		this.mockMvc.perform(get(url))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", hasSize(0)));
	}

	@ParameterizedTest
	@MethodSource("queryParamsAndExpectedSizesProviders")
	void testFindAllByFilters(final String queryParam, final int expectedSize) throws Exception {
		this.productRepository.deleteAll();

		this.productRepository.save(new Product("Desodorante Verde", "Desodorante Maravilhoso Verde", BigDecimal.valueOf(12.30)));
		this.productRepository.save(new Product("Desodorante", "Desodorante Verde", BigDecimal.valueOf(15.12)));
		this.productRepository.save(new Product("Sabão", "Sabão top", BigDecimal.valueOf(25.10)));

		this.mockMvc.perform(get("/products/search?" + queryParam))
				.andExpect(jsonPath("$", hasSize(expectedSize)));
	}

	static Stream<Arguments> queryParamsAndExpectedSizesProviders() {
		return Stream.of(
				arguments("q=Desodorante Verde",2),
				arguments("q=Desodorante Verde&min_price=12.00",2),
				arguments("q=Desodorante Verde&min_price=14.00",1),
				arguments("min_price=12.00&max_price=26.00",3),
				arguments("max_price=26.00",3),
				arguments("max_price=14.00",1),
				arguments("min_price=15.13",1),
				arguments("min_price=27.00",0),
				arguments("q=Sabão",1),
				arguments("q=Sabão top",1)
		);
	}


	void findAllByFiltersNameOrDescriptionAndMinPrice() throws Exception {
		this.productRepository.save(new Product("Desodorante Verde", "Desodorante Maravilhoso Verde", BigDecimal.valueOf(12.30)));
		this.productRepository.save(new Product("Desodorante", "Desodorante Verde", BigDecimal.valueOf(15.12)));
		this.productRepository.save(new Product("Sabão", "Sabão top", BigDecimal.valueOf(25.10)));

		this.mockMvc.perform(get("/products/search?q=Desodorante Verde&min_price=12.00"))
				.andExpect(jsonPath("$", hasSize(3)));
	}
	@Test
	void findAllByFiltersMinPriceAndMaxPrice() throws Exception {
		this.productRepository.save(new Product("Desodorante Verde", "Desodorante Maravilhoso Verde", BigDecimal.valueOf(12.30)));
		this.productRepository.save(new Product("Desodorante", "Desodorante Verde", BigDecimal.valueOf(15.12)));
		this.productRepository.save(new Product("Sabão", "Sabão top", BigDecimal.valueOf(25.10)));

		this.mockMvc.perform(get("/products/search?min_price=12.00&max_price=26.00"))
				.andExpect(jsonPath("$", hasSize(3)));
	}
	@Test
	void findAllByFiltersMaxPrice() throws Exception {
		this.productRepository.save(new Product("Desodorante Verde", "Desodorante Maravilhoso Verde", BigDecimal.valueOf(12.30)));
		this.productRepository.save(new Product("Desodorante", "Desodorante Verde", BigDecimal.valueOf(15.12)));
		this.productRepository.save(new Product("Sabão", "Sabão top", BigDecimal.valueOf(25.10)));

		this.mockMvc.perform(get("/products/search?max_price=26.00"))
				.andExpect(jsonPath("$", hasSize(3)));
	}
	@Test
	void findAllByFiltersName() throws Exception {
		this.productRepository.save(new Product("Desodorante Verde", "Desodorante Maravilhoso Verde", BigDecimal.valueOf(12.30)));
		this.productRepository.save(new Product("Desodorante", "Desodorante Verde", BigDecimal.valueOf(15.12)));
		this.productRepository.save(new Product("Sabão", "Sabão top", BigDecimal.valueOf(25.10)));

		this.mockMvc.perform(get("/products/search?q=Sabão"))
				.andExpect(jsonPath("$", hasSize(1)));
	}
	@Test
	void findAllByFiltersDescription() throws Exception {
		this.productRepository.save(new Product("Desodorante Verde", "Desodorante Maravilhoso Verde", BigDecimal.valueOf(12.30)));
		this.productRepository.save(new Product("Desodorante", "Desodorante Verde", BigDecimal.valueOf(15.12)));
		this.productRepository.save(new Product("Sabão", "Sabão top", BigDecimal.valueOf(25.10)));

		this.mockMvc.perform(get("/products/search?q=Sabão top"))
				.andExpect(jsonPath("$", hasSize(1)));
	}

	@Test
	void testDeleteByShouldReturnOk() throws Exception {
		final Product product = new Product("Desodorante", "DesodoranteVerde", BigDecimal.valueOf(14L));
		final Product saveProduct = this.productRepository.save(product);

		final String url = "/products/" + saveProduct.getId();
		this.mockMvc.perform(delete(url))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	void testDeleteByIdShouldReturnNotFound() throws Exception {
		final String url = "/products/1241";

		this.mockMvc.perform(delete(url))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

}
