package compasso.test.springboot.catalogoprodutos.rabbitmq;


import java.util.concurrent.CountDownLatch;

public interface Receiver<T> {
	public void receiveMessage(T message);
}
