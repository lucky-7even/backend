package com.luckyseven.backend.domain.product;

import com.luckyseven.backend.domain.product.entity.Product;
import com.luckyseven.backend.domain.product.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Page<Product> findByProductIdLessThanOrderByProductIdDesc(Long productId, Pageable pageable);

	Page<Product> findByProductIdLessThanAndNameContainsOrderByProductIdDesc(Long productId, String name, Pageable pageable);

	Page<Product> findByProductIdLessThanAndCategoryOrderByProductIdDesc(Long productId, Category category, Pageable pageable);
}
