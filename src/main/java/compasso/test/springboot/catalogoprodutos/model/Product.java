package compasso.test.springboot.catalogoprodutos.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@ToString
@Setter
@Getter
@Entity
@Table
@Builder
public class Product implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;
	private BigDecimal price;

	public Product() {
	}

	public Product(String name, String description, BigDecimal price) {
		this.name = name;
		this.description = description;
		this.price = price;
	}

	public Product(Long id, String name, String description, BigDecimal price) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
	}

}
