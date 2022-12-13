package com.luckyseven.backend.domain.product;

import java.util.List;

import com.luckyseven.backend.domain.product.dto.ProductReplyRequestDto;
import com.luckyseven.backend.domain.product.dto.ProductReplyResponseDto;
import com.luckyseven.backend.domain.product.dto.ProductRequest;
import com.luckyseven.backend.domain.product.dto.ProductResponse;
import com.luckyseven.backend.domain.product.model.Category;
import com.luckyseven.backend.domain.product.model.SearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.luckyseven.backend.global.config.CommonApiResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("api/products")
@Api(tags = "물품 APIs")
@RequiredArgsConstructor
public class ProductController {
	private final ProductService productService;

	@GetMapping
	@ApiOperation(value = "물품 전체 리스트 조회, 물품 검색 리스트 조회, 카테고리 클릭 시 리스트 조회")
	public ResponseEntity<CommonApiResponse<List<ProductResponse>>> products(
		@RequestParam Long productId,
		@RequestParam int size,
		@RequestParam(required = false) String name,
		@RequestParam(required = false) Category category,
		@RequestParam(required = false) SearchCondition search){

		if (category != null) return ResponseEntity.ok(CommonApiResponse.of(productService.findCategoryProducts(productId, category, search, size)));
		else if (name != null) return ResponseEntity.ok(CommonApiResponse.of(productService.searchProducts(productId, name, search, size)));
		return ResponseEntity.ok(CommonApiResponse.of(productService.findProducts(productId, size)));
	}

	@PostMapping(consumes = {
			MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE})
	@ApiOperation(value = "물품 등록")
	public ResponseEntity<CommonApiResponse<ProductResponse>> makeProduct(
		@ApiParam(value = "example: {\"address\": \"test@naver.com\"}")
		@ApiIgnore Authentication authentication,
		@Valid @RequestPart ProductRequest productRequest,
		@RequestPart(required = false) List<MultipartFile> multipartFiles) {
		return ResponseEntity.ok(CommonApiResponse.of(productService.makeProduct(authentication.getName(), productRequest, multipartFiles)));
	}

	@GetMapping("{productId}")
	@ApiOperation(value = "물품 상세 페이지 조회")
	public ResponseEntity<CommonApiResponse<ProductResponse>> findProduct(
			@PathVariable Long productId) {
		return ResponseEntity.ok(CommonApiResponse.of(productService.findProduct(productId)));
	}
	
	@PostMapping("{productId}")
	@ApiOperation(value = "물품 찜")
	public ResponseEntity<String> likeProduct(
			@ApiIgnore Authentication authentication,
			@PathVariable Long productId) {
		productService.likeProduct(authentication.getName(), productId);
		return ResponseEntity.ok(productId + "번 물품의 찜 및 찜 취소 처리가 완료되었습니다.");
	}
	
	@PostMapping("{productId}/replies")
	@ApiOperation(value = "물품 댓글")
	public ResponseEntity<CommonApiResponse<ProductReplyResponseDto>> makeProductReply(
			@ApiIgnore Authentication authentication,
			@PathVariable Long productId,
			@RequestBody ProductReplyRequestDto productReplyRequestDto) {
		return ResponseEntity.ok(CommonApiResponse.of(productService.makeProductReply(authentication.getName(), productId, productReplyRequestDto)));
	}
}
