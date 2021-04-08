package compasso.test.springboot.catalogoprodutos.repository.specifications;


import compasso.test.springboot.catalogoprodutos.model.Product;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;

public class ProductSpecifications {
	private ProductSpecifications() {
	}

	public static Specification<Product> productWithMinMaxPrice(BigDecimal minPrice, BigDecimal maxPrice) {
		return (root, query, cb) -> {
			if (minPrice == null && maxPrice == null) {
				return cb.isTrue(cb.literal(true)); // always true = no filtering
			}
			final Predicate minPricePredicate = cb.greaterThanOrEqualTo(root.get("price"), minPrice);
			if (maxPrice == null) {
				return minPricePredicate;
			}

			final Predicate maxPricePredicate = cb.lessThanOrEqualTo(root.get("price"), maxPrice);
			if (minPrice == null) {
				return maxPricePredicate;
			}

			return cb.between(root.get("price"), minPrice, maxPrice);

		};
	}


	public static Specification<Product> produtoWithNameOrDescription(String nameOrDescription) {
		return (root, query, cb) -> {
			if (nameOrDescription == null) {
				return cb.isTrue(cb.literal(true)); // always true = no filtering
			}
			final Predicate namePredicate = cb.equal(root.get("name"), nameOrDescription);

			final Predicate descriptionPredicate = cb.equal(root.get("description"), nameOrDescription);

			return cb.or(namePredicate, descriptionPredicate);
		};
	}

}
