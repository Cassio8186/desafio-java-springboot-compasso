package compasso.test.springboot.catalogoprodutos.model.dto;

import compasso.test.springboot.catalogoprodutos.model.Produto;

import java.math.BigDecimal;

public class ProdutoSaveDTO {
	private String name;
	private String description;
	private BigDecimal price;

	public ProdutoSaveDTO() {
	}

	public ProdutoSaveDTO(String name, String description, BigDecimal price) {
		this.name = name;
		this.description = description;
		this.price = price;
	}

	public static ProdutoSaveDTO fromEntity(Produto product) {
		return new ProdutoSaveDTO(product.getName(), product.getDescription(), product.getPrice());
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
		return "ProdutoSaveDTO{" +
				"name='" + name + '\'' +
				", description='" + description + '\'' +
				", price=" + price +
				'}';
	}

	public Produto toEntity() {
		return new Produto(this.getName(), this.getDescription(), this.getPrice());
	}
}
