package com.luckyseven.backend.domain.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByNameContains(String name);

	List<Product> findByCategory(Category category);
}
