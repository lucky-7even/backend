package com.luckyseven.backend.domain.product.entity;

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
public class ProductLikes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productLikesId;

    private boolean isLike;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public void setLike() {
        this.isLike = true;
    }

    public void cancelLike() {
        this.isLike = false;
    }
}
