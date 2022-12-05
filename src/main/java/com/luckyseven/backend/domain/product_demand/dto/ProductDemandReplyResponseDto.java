package com.luckyseven.backend.domain.product_demand.dto;

import com.luckyseven.backend.domain.product_demand.entity.ProductDemandLikes;
import com.luckyseven.backend.domain.product_demand.entity.ProductDemandReply;
import com.luckyseven.backend.global.util.Time;
import io.netty.util.DomainMappingBuilder;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductDemandReplyResponseDto {
    private Long id;

    private String replyer;

    private String content;

    private String term;

    private Long productDemandReplyId;

    private List<ProductDemandReplyResponseDto> children;

    public static ProductDemandReplyResponseDto of(ProductDemandReply productDemandReply) {
        List<ProductDemandReplyResponseDto> children = new ArrayList<>();
        return ProductDemandReplyResponseDto.builder()
                .id(productDemandReply.getProductDemandReplyId())
                .replyer(productDemandReply.getMember().getNickname())
                .content(productDemandReply.getContent())
                .term(Time.calculateTime(Timestamp.valueOf(productDemandReply.getCreatedAt())))
                .children(children)
                .build();
    }
}
