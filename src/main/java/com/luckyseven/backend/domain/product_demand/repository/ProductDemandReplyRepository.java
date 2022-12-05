package com.luckyseven.backend.domain.product_demand.repository;

import com.luckyseven.backend.domain.product_demand.entity.ProductDemandReply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDemandReplyRepository extends JpaRepository<ProductDemandReply, Long> {

    ProductDemandReply findByProductDemandReplyId(Long productDemandReplyId);
}
