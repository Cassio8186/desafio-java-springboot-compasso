package compasso.test.springboot.catalogoprodutos.rabbitmq.sender;

import com.github.javafaker.Faker;
import compasso.test.springboot.catalogoprodutos.model.dto.ProductSaveDTO;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductSaveSender {

	public static final Faker faker = Faker.instance();
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private Queue productSaveQueue;

	public void send(ProductSaveDTO productSaveDTO){
		final BigDecimal randomPrice = BigDecimal.valueOf(faker.number().randomDouble(2, 0L, 10000L));
		this.rabbitTemplate.convertAndSend(productSaveQueue.getName(), productSaveDTO);

	}

}
