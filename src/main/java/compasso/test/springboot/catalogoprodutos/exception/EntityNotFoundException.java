package compasso.test.springboot.catalogoprodutos.exception;

public class EntityNotFoundException extends RuntimeException{

	public EntityNotFoundException(String message) {
		super(message);
	}

}
