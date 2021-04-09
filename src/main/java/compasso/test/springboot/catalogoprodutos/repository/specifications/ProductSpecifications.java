package compasso.test.springboot.catalogoprodutos.repository.specifications;


import compasso.test.springboot.catalogoprodutos.model.Product;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.Arrays;

public class ProductSpecifications {
	private ProductSpecifications() {
	}

	private static String getLikeFilter(String searchValue) {
		StringBuilder likeFilterBuilder = new StringBuilder();
		likeFilterBuilder.append("%");
		Arrays.stream(searchValue.split(" ")).forEach(word -> {
			likeFilterBuilder.append(word.toLowerCase()).append("%");
		});

		return likeFilterBuilder.toString();

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

	public static Specification<Product> produtoWithNameOrDescription(final String nameOrDescription) {
		return (root, query, cb) -> {
			if (nameOrDescription == null) {
				return cb.isTrue(cb.literal(true)); // always true = no filtering
			}


			final String nameOrDescriptionLikeValue = getLikeFilter(nameOrDescription);
			final Predicate namePredicate = cb.like(cb.lower(root.get("name")), nameOrDescriptionLikeValue);

			final Predicate descriptionPredicate = cb.like(cb.lower(root.get("description")), nameOrDescriptionLikeValue);

			return cb.or(namePredicate, descriptionPredicate);
		};
	}

}
