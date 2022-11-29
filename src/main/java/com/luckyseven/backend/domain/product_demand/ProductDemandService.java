package com.luckyseven.backend.domain.product_demand;

import com.luckyseven.backend.domain.member.MemberRepository;
import com.luckyseven.backend.domain.member.entity.Member;
import com.luckyseven.backend.domain.product_demand.dto.ProductDemandRequestDto;
import com.luckyseven.backend.domain.product_demand.dto.ProductDemandResponseDto;
import com.luckyseven.backend.domain.product_demand.entity.ProductDemand;
import com.luckyseven.backend.domain.product_demand.entity.ProductDemandImages;
import com.luckyseven.backend.global.config.s3.AwsS3ServiceImpl;
import com.luckyseven.backend.global.error.ErrorCode;
import com.luckyseven.backend.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDemandService {
    private final ProductDemandRepository productDemandRepository;
    private final ProductDemandImagesRespository productDemandImagesRespository;
    private final MemberRepository memberRepository;
    private final AwsS3ServiceImpl awsS3Service;

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
}
