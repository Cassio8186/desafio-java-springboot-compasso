package compasso.test.springboot.catalogoprodutos.rabbitmq;

import com.github.javafaker.Faker;
import compasso.test.springboot.catalogoprodutos.model.dto.ProductSaveDTO;
import compasso.test.springboot.catalogoprodutos.rabbitmq.sender.ProductSaveSenderTest;
import compasso.test.springboot.catalogoprodutos.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;


@SpringBootTest
@ActiveProfiles("h2")
class ProductSaveReceiverTest {

	@Autowired
	private ProductSaveSenderTest productSaveSenderTest;

	@Autowired
	private ProductSaveReceiver productSaveClient;

	@Autowired
	private ProductRepository productRepository;

	@BeforeEach
	void beforeEach() {
		this.productRepository.deleteAll();
	}

	@Test
	void receiveMessage_WhenReceiveProductDTOMessage_ExpectedCallProductService() throws InterruptedException {
		final BigDecimal randomPrice = BigDecimal.valueOf(Faker.instance().number().randomDouble(2, 0L, 1000L));
		final ProductSaveDTO productSaveDTO = new ProductSaveDTO("Product from rabbit", "Product description", randomPrice);
		this.productSaveSenderTest.send(productSaveDTO);
		await().atMost(10, SECONDS).until(this::productIsSavedInDatabase);
	}

	private Boolean productIsSavedInDatabase() {
		return this.productRepository.findAll().size() == 1;
	}
}
