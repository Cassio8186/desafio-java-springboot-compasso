package compasso.test.springboot.catalogoprodutos.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO {

	@JsonProperty("status_code")
	private Integer statusCode;
	private String message;

}
