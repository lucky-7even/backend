package com.luckyseven.backend.domain.product_demand.entity;

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
public class ProductDemandImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productDemandImagesId;

    private String productImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductDemand productDemand;
}
