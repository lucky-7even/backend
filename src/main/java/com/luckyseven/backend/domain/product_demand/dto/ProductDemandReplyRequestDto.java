package com.luckyseven.backend.domain.product_demand.dto;

import com.luckyseven.backend.domain.member.entity.Member;
import com.luckyseven.backend.domain.product_demand.entity.ProductDemand;
import com.luckyseven.backend.domain.product_demand.entity.ProductDemandReply;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductDemandReplyRequestDto {

    @ApiModelProperty(value = "부모 댓글 id")
    private Long productDemandReplyId;
    
    @ApiModelProperty(value = "댓글내용", example = "빌려줄래짱짱")
    private String content;

    @ApiModelProperty(hidden = true)
    private Member member;

    @ApiModelProperty(hidden = true)
    private ProductDemand productDemand;

    public static ProductDemandReply toProductDemandReply(ProductDemandReplyRequestDto dto) {
        return ProductDemandReply.builder()
                .content(dto.getContent())
                .member(dto.getMember())
                .productDemand(dto.getProductDemand())
                .build();
    }
}
