package compasso.test.springboot.catalogoprodutos.util;

import com.github.javafaker.Commerce;
import com.github.javafaker.Faker;
import compasso.test.springboot.catalogoprodutos.model.Product;

import java.math.BigDecimal;

public class ProductUtil {

	private static final Faker faker = Faker.instance();

	public static Product generateProduct(Boolean withId) {
		final Commerce commerceFaker = faker.commerce();
		final String productName = commerceFaker.productName();

		final Product.ProductBuilder builder = Product.builder();

		if (withId) {
			builder.id(faker.number().randomNumber());
		}

		return builder
				.name(productName)
				.description(productName + " - " + commerceFaker.department())
				.price(BigDecimal.valueOf(faker.number().randomDouble(2, 1, 10000)))
				.build();

	}
	public static Product cloneProduct(Product product) {
		return new Product(product.getId(), product.getName(), product.getDescription(), product.getPrice());
	}
}
