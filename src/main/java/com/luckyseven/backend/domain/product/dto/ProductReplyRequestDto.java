package com.luckyseven.backend.domain.product.dto;


import com.luckyseven.backend.domain.member.entity.Member;
import com.luckyseven.backend.domain.product.entity.Product;
import com.luckyseven.backend.domain.product.entity.ProductReply;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductReplyRequestDto {
    @ApiModelProperty(value = "댓글내용", example = "빌려줄래짱짱")
    private String content;

    @ApiModelProperty(hidden = true)
    private Member member;

    @ApiModelProperty(hidden = true)
    private Product product;

    public static ProductReply toProductReply(ProductReplyRequestDto dto) {
        return ProductReply.builder()
                .content(dto.getContent())
                .member(dto.getMember())
                .product(dto.getProduct())
                .build();
    }
}
