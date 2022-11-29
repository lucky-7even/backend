package com.luckyseven.backend.domain.product_demand.dto;

import com.luckyseven.backend.domain.product_demand.entity.ProductDemand;
import com.luckyseven.backend.domain.product_demand.model.Category;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductDemandResponseDto {
    private Category category;

    private String productName;

    private Long price;

    private String description;

    private List<String> productImages;

    public static ProductDemandResponseDto of(ProductDemand productDemand, List<String> productImagesUrls) {
        return ProductDemandResponseDto.builder()
                .category(productDemand.getCategory())
                .productName(productDemand.getProductName())
                .price(productDemand.getPrice())
                .description(productDemand.getDescription())
                .productImages(productImagesUrls)
                .build();
    }
}
