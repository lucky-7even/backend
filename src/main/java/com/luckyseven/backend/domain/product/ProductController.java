package com.luckyseven.backend.domain.product;

import static com.luckyseven.backend.global.config.CommonApiResponse.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luckyseven.backend.global.config.CommonApiResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("api")
@Api(tags = "물품 APIs")
public class ProductController {
	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping("products")
	@ApiOperation(value = "물품 전체 리스트 조회, 물품 검색 리스트 조회, 카테고리 클릭 시 리스트 조회")
	public CommonApiResponse<List<ProductResponse>> products(
		@RequestParam Long id,
		@RequestParam int size,
		@RequestParam(required = false) String name,
		@RequestParam(required = false) Category category){
		if (category != null) {
			return of(
				productService.findByIdLessThanAndCategoryOrderByIdDesc(id, category, PageRequest.of(0, size))
					.stream()
					.map(ProductResponse::new)
					.collect(Collectors.toList())
			);
		}
		else if (name != null) {
			return of(
				productService.findByIdLessThanAndNameContainsOrderByIdDesc(id, name, PageRequest.of(0, size))
					.stream()
					.map(ProductResponse::new)
					.collect(Collectors.toList())
			);
		}
		return of(
			productService.findByIdLessThanOrderByIdDesc(id, PageRequest.of(0, size))
				.stream()
				.map(ProductResponse::new)
				.collect(Collectors.toList())
		);
	}

	@PostMapping("products")
	@ApiOperation(value = "물품 등록")
	public CommonApiResponse<ProductResponse> producting(
		@ApiParam(value = "example: {\"address\": \"test@naver.com\"}")
		@Valid @RequestBody ProductRequest productRequest) {
		return of(
			new ProductResponse(
				productService.write(productRequest)
			)
		);
	}

	@GetMapping("products/{productId}")
	@ApiOperation(value = "물품 상세 페이지 조회")
	public CommonApiResponse<ProductResponse> getProduct(
		@ApiParam(value = "물품 아이디 PK", example = "1L") Long productId) {
		return of(
			new ProductResponse(
				productService.findOne(productId)
			)
		);
	}
}
