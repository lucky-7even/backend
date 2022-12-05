package com.luckyseven.backend.domain.product_demand;

import com.luckyseven.backend.domain.product_demand.dto.ProductDemandReplyRequestDto;
import com.luckyseven.backend.domain.product_demand.dto.ProductDemandReplyResponseDto;
import com.luckyseven.backend.domain.product_demand.dto.ProductDemandRequestDto;
import com.luckyseven.backend.domain.product_demand.dto.ProductDemandResponseDto;
import com.luckyseven.backend.global.config.CommonApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/productDemands")
@Api(tags = "구매 요청")
public class ProductDemandController {
    private final ProductDemandService productDemandService;

    @PostMapping(consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiOperation(value = "물품 요청 등록")
    public ResponseEntity<CommonApiResponse<ProductDemandResponseDto>> makeProductDemands(
            @ApiIgnore Authentication authentication,
            @RequestPart ProductDemandRequestDto productDemandRequestDto,
            @RequestPart(required = false) List<MultipartFile> multipartFiles) {
        return ResponseEntity.ok(CommonApiResponse.of(productDemandService.makeProdcutDemands(authentication.getName(), productDemandRequestDto, multipartFiles)));
    }
    
    @GetMapping
    @ApiOperation(value = "물품 요청 전체보기")
    public ResponseEntity<CommonApiResponse<List<ProductDemandResponseDto>>> showProductDemands() {
        return ResponseEntity.ok(CommonApiResponse.of(productDemandService.showProductDemands()));
    }
    
    @PostMapping("{productDemandId}")
    @ApiOperation(value = "물품 요청글 좋아요")
    public ResponseEntity<String> likeProductDemand(
            @ApiIgnore Authentication authentication,
            @PathVariable Long productDemandId) {
        productDemandService.likeProductDemand(authentication.getName(), productDemandId);
        return ResponseEntity.ok(productDemandId + "번 물품의 좋아요 및 취소 처리가 완료되었습니다.");
    }

    @PostMapping("{productDemandId}/replies")
    @ApiOperation(value = "물품 요청 댓글 작성")
    public ResponseEntity<CommonApiResponse<ProductDemandReplyResponseDto>> makeProductDemandReply(
            @ApiIgnore Authentication authentication,
            @PathVariable Long productDemandId,
            @RequestBody ProductDemandReplyRequestDto productDemandReplyRequestDto) {
        return ResponseEntity.ok(CommonApiResponse.of(productDemandService.makeProductDemandReply(authentication.getName(), productDemandId, productDemandReplyRequestDto)));
    }
}
