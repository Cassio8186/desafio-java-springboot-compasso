package compasso.test.springboot.catalogoprodutos.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorDTO {

	@JsonProperty("status_code")
	private Integer statusCode;
	private String message;

	public ErrorDTO() {
	}

	public ErrorDTO(Integer statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
