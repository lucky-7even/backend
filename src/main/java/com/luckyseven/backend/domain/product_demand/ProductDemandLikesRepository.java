package com.luckyseven.backend.domain.product_demand;

import com.luckyseven.backend.domain.product_demand.entity.ProductDemandLikes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDemandLikesRepository extends JpaRepository<ProductDemandLikes, Long> {
    boolean existsByMember_Email(String email);

    ProductDemandLikes findByProductDemand_ProductDemandId(Long productDemandId);

    Long countByProductDemand_ProductDemandIdAndIsLikeTrue(Long productDemandId);
}
