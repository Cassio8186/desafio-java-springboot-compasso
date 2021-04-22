package compasso.test.springboot.catalogoprodutos.model.dto;

import compasso.test.springboot.catalogoprodutos.model.Product;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSaveDTO implements Serializable {

	@NotEmpty(message = "deve ser preenchido")
	@ApiModelProperty(example = "\"Desodorante\"", required = true)
	private String name;

	@NotEmpty(message = "deve ser preenchido")
	@ApiModelProperty(example = "\"Desodorante Azul\"", required = true)
	private String description;

	@NotNull(message = "deve ser preenchido")
	@ApiModelProperty(example = "15.23", required = true)
	@DecimalMin(value = "0.0", message = "Deve ser acima de 0 reais.", inclusive = false)
	private BigDecimal price;

	public static ProductSaveDTO fromEntity(Product product) {
		return new ProductSaveDTO(product.getName(), product.getDescription(), product.getPrice());
	}

	public Product toEntity() {
		return new Product(this.getName(), this.getDescription(), this.getPrice());
	}
}
