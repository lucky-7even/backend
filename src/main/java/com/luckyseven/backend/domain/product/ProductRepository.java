package com.luckyseven.backend.domain.product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	Page<Product> findByIdLessThanOrderByIdDesc(Long id, Pageable pageable);

	List<Product> findByIdLessThanAndNameContainsOrderByIdDesc(Long id, String name, Pageable pageable);

	Page<Product> findByIdLessThanAndCategoryOrderByIdDesc(Long id, Category category, Pageable pageable);
}
