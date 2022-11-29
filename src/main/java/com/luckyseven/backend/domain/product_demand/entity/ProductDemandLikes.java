package com.luckyseven.backend.domain.product_demand.entity;

import com.luckyseven.backend.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDemandLikes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productDemandLikesId;

    private boolean isLike;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductDemand productDemand;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public void setLike() {
        this.isLike = true;
    }

    public void cancelLike() {
        this.isLike = false;
    }
}
