package com.luckyseven.backend.domain.product_demand.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luckyseven.backend.domain.product_demand.ProductDemandLikesRepository;
import com.luckyseven.backend.domain.product_demand.entity.ProductDemand;
import com.luckyseven.backend.domain.product_demand.entity.ProductDemandImages;
import com.luckyseven.backend.domain.product_demand.entity.ProductDemandLikes;
import com.luckyseven.backend.domain.product_demand.model.Category;
import com.luckyseven.backend.global.util.Time;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDemandResponseDto {
    private Long productDemandId;

    private Category category;

    private Long period;

    private boolean isPrice;

    private String productName;

    private Long price;

    private String description;

    private List<String> productImages;

    private String buyer;

    private String buyerLocation;

    private String term;

    private Long likes;

    public ProductDemandResponseDto(ProductDemand productDemand) {
        this.productDemandId = productDemand.getProductDemandId();
        this.category = productDemand.getCategory();
        this.period = productDemand.getPeriod();
        this.isPrice = productDemand.isPrice();
        this.productName = productDemand.getProductName();
        this.price = productDemand.getPrice();
        this.description = productDemand.getDescription();
        this.productImages = productDemand.getProductDemandImagesList()
                .stream().map(ProductDemandImages::getProductImageUrl).collect(Collectors.toList());
        this.buyer = productDemand.getMember().getNickname();
        this.buyerLocation = productDemand.getMember().getLocation();
        this.term = Time.calculateTime(java.sql.Timestamp.valueOf(productDemand.getCreatedAt()));
    }

    public static ProductDemandResponseDto of(ProductDemand productDemand, List<String> productImagesUrls) {
        return ProductDemandResponseDto.builder()
                .productDemandId(productDemand.getProductDemandId())
                .category(productDemand.getCategory())
                .period(productDemand.getPeriod())
                .isPrice(productDemand.isPrice())
                .productName(productDemand.getProductName())
                .price(productDemand.getPrice())
                .description(productDemand.getDescription())
                .productImages(productImagesUrls)
                .build();
    }
}
