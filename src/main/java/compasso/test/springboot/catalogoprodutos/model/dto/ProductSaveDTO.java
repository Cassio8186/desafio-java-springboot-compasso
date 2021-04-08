package compasso.test.springboot.catalogoprodutos.model.dto;

import compasso.test.springboot.catalogoprodutos.model.Product;

import java.math.BigDecimal;

public class ProductSaveDTO {
	private String name;
	private String description;
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