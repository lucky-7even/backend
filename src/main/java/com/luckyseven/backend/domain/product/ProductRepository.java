package com.luckyseven.backend.domain.product;

import java.util.List;

import com.luckyseven.backend.domain.product.entity.Product;
import com.luckyseven.backend.domain.product.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Page<Product> findAll(Pageable pageable);

	Page<Product> findByNameContaining(String name, Pageable pageable);

	Page<Product> findByCategory(Category category, Pageable pageable);
}
