package compasso.test.springboot.catalogoprodutos.model.dto;

import compasso.test.springboot.catalogoprodutos.model.Product;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProductSaveDTO {

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

	public ProductSaveDTO() {
	}

	public ProductSaveDTO(String name, String description, BigDecimal price) {
		this.name = name;
		this.description = description;
		this.price = price;
	}

	public static ProductSaveDTO fromEntity(Product product) {
		return new ProductSaveDTO(product.getName(), product.getDescription(), product.getPrice());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "ProductSaveDTO{" +
				"name='" + name + '\'' +
				", description='" + description + '\'' +
				", price=" + price +
				'}';
	}

	public Product toEntity() {
		return new Product(this.getName(), this.getDescription(), this.getPrice());
	}
}
