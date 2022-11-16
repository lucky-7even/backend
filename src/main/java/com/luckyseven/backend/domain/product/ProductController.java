package com.luckyseven.backend.domain.product;

import static com.luckyseven.backend.global.config.CommonApiResponse.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

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

	@PostConstruct
	public void init() {
		productService.write(new ProductRequest(1L, "냉장고", Category.AA, 1000, "좋아요"));
		productService.write(new ProductRequest(2L, "소파", Category.BB, 5500, "짱이에요"));
		productService.write(new ProductRequest(3L, "에어팟1", Category.CC, 10000, "빌려드립니다."));
		productService.write(new ProductRequest(4L, "에어팟2", Category.DD, 15000, "오래 빌려드립니다."));
	}

	@GetMapping("product")
	@ApiOperation(value = "물품 전체 리스트, 검색 조회")
	public CommonApiResponse<List<ProductResponse>> products(@RequestParam(required = false) String name) {
		if (name != null) {
			return of(
				productService.findByNameContains(name)
					.stream()
					.map(ProductResponse::new)
					.collect(Collectors.toList())
			);
		}
		return of(
			productService.findAll()
				.stream()
				.map(ProductResponse::new)
				.collect(Collectors.toList()
				)
		);
	}

	@PostMapping("product")
	@ApiOperation(value = "물품 등록")
	public CommonApiResponse<ProductResponse> producting(
		@ApiParam(value = "example: {\"address\": \"test@naver.com\"}")
		@RequestBody ProductRequest productRequest) {
		return of(
			new ProductResponse(
				productService.write(productRequest)
			)
		);
	}

	@GetMapping("product/{productId}")
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
