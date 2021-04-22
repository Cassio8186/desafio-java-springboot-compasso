package compasso.test.springboot.catalogoprodutos.config.rabbitmq;

import com.rabbitmq.client.AMQP;
import compasso.test.springboot.catalogoprodutos.rabbitmq.ProductSaveReceiver;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitReceiverConfiguration {


	@Bean
	public Queue productSaveQueue(@Value("${queue.product.save.name}") String queueName) {
		return new Queue(queueName);
	}

	@Bean
	public ProductSaveReceiver productSaveClient(ProductSaveReceiver productSaveReceiver) {
		return productSaveReceiver;
	}
}
