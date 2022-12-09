package com.luckyseven.backend.domain.product.repository;

import com.luckyseven.backend.domain.product.entity.ProductLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductLikesRepository extends JpaRepository<ProductLikes, Long> {
    boolean existsByMember_Email(String email);

    @Query("select p from ProductLikes p " +
            "join fetch p.product " +
            "where p.product.productId = :productId")
    ProductLikes findByProduct_ProductId(Long productId);

    Long countByProduct_ProductIdAndIsLikeTrue(Long productId);
}
