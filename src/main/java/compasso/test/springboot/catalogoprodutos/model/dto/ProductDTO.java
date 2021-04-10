package compasso.test.springboot.catalogoprodutos.model.dto;

import compasso.test.springboot.catalogoprodutos.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
	private String id;
	private String name;
	private String description;
	private BigDecimal price;

	public static ProductDTO fromEntity(Product product) {
		return new ProductDTO(product.getId().toString(), product.getName(), product.getDescription(), product.getPrice());
	}

	public static List<ProductDTO> fromEntity(List<Product> products) {
		return products.stream().map(ProductDTO::fromEntity).collect(Collectors.toList());
	}

}
