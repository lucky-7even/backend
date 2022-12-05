package com.luckyseven.backend.domain.product_demand.dto;

import com.luckyseven.backend.domain.member.entity.Member;
import com.luckyseven.backend.domain.product_demand.entity.ProductDemand;
import com.luckyseven.backend.domain.product_demand.model.Category;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductDemandRequestDto {

    @ApiModelProperty(example = "LIVING")
    private Category category;

    @ApiModelProperty(example = "아무물건")
    private String productName;

    @ApiModelProperty(value = "기간", example = "3")
    private Long period;

    @ApiModelProperty(value = "가격협의여부")
    private boolean isPrice;

    @ApiModelProperty(example = "4500")
    private Long price;

    @ApiModelProperty(example = "예시에용")
    private String description;

    @ApiModelProperty(hidden = true)
    private Member member;

    public static ProductDemand toProductDemand(ProductDemandRequestDto dto) {
        return ProductDemand.builder()
                .category(dto.category)
                .productName(dto.productName)
                .period(dto.period)
                .isPrice(dto.isPrice)
                .price(dto.price)
                .description(dto.description)
                .member(dto.member)
                .build();
    }
}
