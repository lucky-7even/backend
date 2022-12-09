package com.luckyseven.backend.domain.product.dto;

import com.luckyseven.backend.domain.product.entity.ProductReply;
import com.luckyseven.backend.global.util.Time;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductReplyResponseDto {
    private Long productReplyId;

    private String replyer;

    private String content;

    private String term;

    public static ProductReplyResponseDto of(ProductReply productReply) {
        return ProductReplyResponseDto.builder()
                .productReplyId(productReply.getProductReplyId())
                .replyer(productReply.getMember().getNickname())
                .content(productReply.getContent())
                .term(Time.calculateTime(Timestamp.valueOf(productReply.getCreatedAt())))
                .build();
    }
}
