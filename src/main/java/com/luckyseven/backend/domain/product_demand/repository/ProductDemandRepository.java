package com.luckyseven.backend.domain.product_demand.repository;

import com.luckyseven.backend.domain.product_demand.entity.ProductDemand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDemandRepository extends JpaRepository<ProductDemand, Long> {
}
