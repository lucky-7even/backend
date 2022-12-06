package com.luckyseven.backend.domain.product_demand;

import com.luckyseven.backend.domain.member.MemberRepository;
import com.luckyseven.backend.domain.member.entity.Member;
import com.luckyseven.backend.domain.product_demand.dto.ProductDemandReplyRequestDto;
import com.luckyseven.backend.domain.product_demand.dto.ProductDemandReplyResponseDto;
import com.luckyseven.backend.domain.product_demand.dto.ProductDemandRequestDto;
import com.luckyseven.backend.domain.product_demand.dto.ProductDemandResponseDto;
import com.luckyseven.backend.domain.product_demand.entity.ProductDemand;
import com.luckyseven.backend.domain.product_demand.entity.ProductDemandImages;
import com.luckyseven.backend.domain.product_demand.entity.ProductDemandLikes;
import com.luckyseven.backend.domain.product_demand.entity.ProductDemandReply;
import com.luckyseven.backend.domain.product_demand.repository.*;
import com.luckyseven.backend.global.config.s3.AwsS3ServiceImpl;
import com.luckyseven.backend.global.error.ErrorCode;
import com.luckyseven.backend.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductDemandService {
    private final ProductDemandRepository productDemandRepository;
    private final ProductDemandImagesRespository productDemandImagesRespository;
    private final ProductDemandLikesRepository productDemandLikesRepository;
    private final ProductDemandReplyRepository productDemandReplyRepository;
    private final ProductDemandReplyCustomRepository productDemandReplyCustomRepository;
    private final MemberRepository memberRepository;
    private final AwsS3ServiceImpl awsS3Service;

    // 물품 요청
    @Transactional
    public ProductDemandResponseDto makeProdcutDemands(String email, ProductDemandRequestDto productDemandRequestDto, List<MultipartFile> multipartFiles) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException(ErrorCode.MEMBER_NOT_FOUND));

        productDemandRequestDto.setMember(member);

        ProductDemand productDemand = ProductDemandRequestDto.toProductDemand(productDemandRequestDto);
        List<String> productDemandImageUrls = awsS3Service.uploadImage(multipartFiles, "productDemand");
        productDemandRepository.save(productDemand);

        for (String productDemandImageUrl : productDemandImageUrls) {
            ProductDemandImages productDemandImages = ProductDemandImages.builder()
                    .productImageUrl(productDemandImageUrl)
                    .productDemand(productDemand)
                    .build();
            productDemandImagesRespository.save(productDemandImages);
        }

        return ProductDemandResponseDto.of(productDemand, productDemandImageUrls);
    }
    
    // 물품 요청글 좋아요
    @Transactional
    public void likeProductDemand(String email, Long productDemandId) {
        Member member = memberRepository.findByEmail(email)
               .orElseThrow(() -> new BadRequestException(ErrorCode.MEMBER_NOT_FOUND));

        ProductDemand productDemand = productDemandRepository.findById(productDemandId)
                .orElseThrow(() -> new BadRequestException(ErrorCode.PRODUCT_DEMAND_NOT_FOUND));

        if (!productDemandLikesRepository.existsByMember_Email(email)) {
            ProductDemandLikes productDemandLikes = ProductDemandLikes.builder()
                    .isLike(true)
                    .productDemand(productDemand)
                    .member(member).build();
            productDemandLikesRepository.save(productDemandLikes);
        } else {
            ProductDemandLikes productDemandLikes = productDemandLikesRepository.findByProductDemand_ProductDemandId(productDemandId);
            if (productDemandLikes.isLike()) {
                productDemandLikes.cancelLike();
            } else if (!productDemandLikes.isLike()) {
                productDemandLikes.setLike();
            }
        }
    }

    // 물품 요청글 보기
    @Transactional(readOnly = true)
    public List<ProductDemandResponseDto> showProductDemands() {
        List<ProductDemand> productDemands = productDemandRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        List<ProductDemandResponseDto> productDemandResponseDtos = productDemands.stream()
                .map(ProductDemandResponseDto::new)
                .collect(Collectors.toList());
        // 요청글 좋아요 set
        productDemandResponseDtos.forEach(likes -> likes.setLikes(productDemandLikesRepository.countByProductDemand_ProductDemandIdAndIsLikeTrue(likes.getProductDemandId())));

        // 요청들 답글들 set
        for (int i = 0; i < productDemands.size(); i++) {
            List<ProductDemandReply> productDemandReplies = productDemandReplyCustomRepository.findAllByProductDemand(productDemandRepository.findById(productDemands.get(i).getProductDemandId()).orElseThrow());
            List<ProductDemandReplyResponseDto> productDemandReplyResponseDtos = new ArrayList<>();
            Map<Long, ProductDemandReplyResponseDto> map = new HashMap<>();

            // 부모, 자식 댓글 set(계층형 댓글)
            productDemandReplies.forEach(c -> {
                        ProductDemandReplyResponseDto pdto = ProductDemandReplyResponseDto.of(c);
                        if (c.getParent() != null) pdto.setProductDemandReplyId(c.getParent().getProductDemandReplyId());

                        map.put(pdto.getId(), pdto);

                        if (c.getParent() != null) map.get(c.getParent().getProductDemandReplyId()).getChildren().add(pdto);
                        else productDemandReplyResponseDtos.add(pdto);
                    }
            );
            productDemandResponseDtos.get(i).setProductDemandReplyResponseDtos(productDemandReplyResponseDtos);
        }
        return productDemandResponseDtos;
    }
    
    // 물품 요청 댓글 작성
    @Transactional
    public ProductDemandReplyResponseDto makeProductDemandReply(String email, Long productDemandId ,ProductDemandReplyRequestDto productDemandReplyRequestDto) {
        ProductDemand productDemand = productDemandRepository.findById(productDemandId)
                .orElseThrow(() -> new BadRequestException(ErrorCode.PRODUCT_DEMAND_NOT_FOUND));

        ProductDemandReply parent = null;
        if (productDemandReplyRequestDto.getProductDemandReplyId() != null) {
            parent = productDemandReplyRepository.findByProductDemandReplyId(productDemandReplyRequestDto.getProductDemandReplyId());
            if (parent == null) {
                throw new BadRequestException(ErrorCode.PRODUCT_DEMAND_REPLY_NOT_FOUND);
            } else if (parent.getProductDemand().getProductDemandId() != productDemandId) {
                throw new BadRequestException(ErrorCode.PRODUCT_DEMAND_REPLY_PARENT_CHILD_NOT_VALID);
            }
        }

        productDemandReplyRequestDto.setMember(memberRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException(ErrorCode.MEMBER_NOT_FOUND)));
        productDemandReplyRequestDto.setProductDemand(productDemand);
        ProductDemandReply productDemandReply = ProductDemandReplyRequestDto.toProductDemandReply(productDemandReplyRequestDto);
        if (parent != null) productDemandReply.updateParent(parent);
        productDemandReplyRepository.save(productDemandReply);

        ProductDemandReplyResponseDto productDemandReplyResponseDto = ProductDemandReplyResponseDto.of(productDemandReply);
        if (parent != null) productDemandReplyResponseDto.setProductDemandReplyId(productDemandReply.getParent().getProductDemandReplyId());

        return productDemandReplyResponseDto;
    }
}
