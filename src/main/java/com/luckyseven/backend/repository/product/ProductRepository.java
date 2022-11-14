package com.luckyseven.backend.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luckyseven.backend.domain.Product;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}