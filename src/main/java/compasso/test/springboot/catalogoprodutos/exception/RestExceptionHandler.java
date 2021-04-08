package compasso.test.springboot.catalogoprodutos.exception;

import compasso.test.springboot.catalogoprodutos.model.dto.ErrorDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(EntityNotFoundException.class)
	public final ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
		final ErrorDTO errorDTO = new ErrorDTO(NOT_FOUND.value(), ex.getMessage());
		return new ResponseEntity<>(errorDTO, new HttpHeaders(), NOT_FOUND);

	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleException(Exception ex) {
		return this.internalExceptionHandler(ex);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
			Exception ex,
			Object body,
			HttpHeaders headers,
			HttpStatus status,
			WebRequest request) {

		return this.internalExceptionHandler(ex);
	}

	private ResponseEntity<Object> internalExceptionHandler(Exception ex) {
		ErrorDTO errorDTO = new ErrorDTO();
		errorDTO.setStatusCode(500);

		ex.printStackTrace();

		final String messagem = "ERRO INTERNO: " + ex.getMessage();
		errorDTO.setMessage(messagem);

		return new ResponseEntity<>(errorDTO, new HttpHeaders(), INTERNAL_SERVER_ERROR);
	}

}
