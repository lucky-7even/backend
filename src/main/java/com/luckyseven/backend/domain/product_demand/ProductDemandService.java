package com.luckyseven.backend.domain.product_demand;

import com.luckyseven.backend.domain.member.MemberRepository;
import com.luckyseven.backend.domain.member.entity.Member;
import com.luckyseven.backend.domain.product_demand.dto.ProductDemandRequestDto;
import com.luckyseven.backend.domain.product_demand.dto.ProductDemandResponseDto;
import com.luckyseven.backend.domain.product_demand.entity.ProductDemand;
import com.luckyseven.backend.domain.product_demand.entity.ProductDemandImages;
import com.luckyseven.backend.domain.product_demand.entity.ProductDemandLikes;
import com.luckyseven.backend.global.config.s3.AwsS3ServiceImpl;
import com.luckyseven.backend.global.error.ErrorCode;
import com.luckyseven.backend.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductDemandService {
    private final ProductDemandRepository productDemandRepository;
    private final ProductDemandImagesRespository productDemandImagesRespository;
    private final ProductDemandLikesRepository productDemandLikesRepository;
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
        productDemandResponseDtos.forEach(likes -> likes.setLikes(productDemandLikesRepository.countByProductDemand_ProductDemandIdAndIsLikeTrue(likes.getProductDemandId())));
        return productDemandResponseDtos;
    }
}
