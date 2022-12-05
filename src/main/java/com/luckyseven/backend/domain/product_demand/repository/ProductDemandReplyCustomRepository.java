package com.luckyseven.backend.domain.product_demand.repository;

import com.luckyseven.backend.domain.product_demand.entity.ProductDemand;
import com.luckyseven.backend.domain.product_demand.entity.ProductDemandReply;
import com.luckyseven.backend.domain.product_demand.entity.QProductDemandReply;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductDemandReplyCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    // 게시글의 댓글 전체 가져오기
    public List<ProductDemandReply> findAllByProductDemand(ProductDemand productDemand){
        return jpaQueryFactory.selectFrom(QProductDemandReply.productDemandReply)
                .leftJoin(QProductDemandReply.productDemandReply.parent)
                .fetchJoin()
                .where(QProductDemandReply.productDemandReply.productDemand.productDemandId
                        .eq(productDemand.getProductDemandId()))
                .orderBy(QProductDemandReply.productDemandReply.parent.productDemandReplyId.asc().nullsFirst(),
                        QProductDemandReply.productDemandReply.createdAt.asc())
                .fetch();
    }
}
