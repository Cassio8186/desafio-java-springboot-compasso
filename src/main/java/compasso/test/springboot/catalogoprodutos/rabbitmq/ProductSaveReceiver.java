package compasso.test.springboot.catalogoprodutos.rabbitmq;

import compasso.test.springboot.catalogoprodutos.model.Product;
import compasso.test.springboot.catalogoprodutos.model.dto.ProductSaveDTO;
import compasso.test.springboot.catalogoprodutos.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@RabbitListener(queues = "catalog-product-save")
@Slf4j
@Component
public class ProductSaveReceiver implements Receiver<ProductSaveDTO> {

	private final ProductService productService;

	public ProductSaveReceiver(ProductService productService) {
		this.productService = productService;
	}

	@RabbitHandler
	public void receiveMessage(ProductSaveDTO productSaveDTO) {
		log.info("Rabbit-MQ: Saving product {}", productSaveDTO);
		try {
			final Product product = productSaveDTO.toEntity();
			this.productService.save(product);
			log.info("Rabbit-MQ: Saving product {} success", productSaveDTO);
		} catch (Exception e) {
			log.error("Rabbit-MQ: Saving product {} failed", productSaveDTO);
			throw e;
		}
	}

}
