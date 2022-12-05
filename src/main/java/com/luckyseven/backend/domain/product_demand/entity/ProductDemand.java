package com.luckyseven.backend.domain.product_demand.entity;

import com.luckyseven.backend.domain.member.entity.Member;
import com.luckyseven.backend.domain.product_demand.model.Category;
import com.luckyseven.backend.global.config.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDemand extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productDemandId;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String productName;

    private Long period;

    private boolean isPrice;

    private Long price;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "productDemand", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ProductDemandImages> productDemandImagesList;

    @OneToMany(mappedBy = "productDemand", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ProductDemandLikes> productDemandLikesList;

    @OneToMany(mappedBy = "productDemand", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ProductDemandReply> productDemandReplyList;
}
