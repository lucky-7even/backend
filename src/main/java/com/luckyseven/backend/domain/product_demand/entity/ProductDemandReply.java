package com.luckyseven.backend.domain.product_demand.entity;

import com.luckyseven.backend.domain.member.entity.Member;
import com.luckyseven.backend.global.config.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDemandReply extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productDemandReplyId;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductDemand productDemand;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductDemandReply parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<ProductDemandReply> children = new ArrayList<>();

    // 부모 댓글 수정
    public void updateParent(ProductDemandReply parent){
        this.parent = parent;
    }
}
