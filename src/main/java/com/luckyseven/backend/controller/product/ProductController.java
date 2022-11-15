package com.luckyseven.backend.controller.product;

import static com.luckyseven.backend.global.config.CommonApiResponse.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luckyseven.backend.global.config.CommonApiResponse;
import com.luckyseven.backend.service.product.ProductService;

@RestController("product")
public class ProductController {
	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public CommonApiResponse<List<ProductResponse>> products(@RequestParam(required = false) String name) {
		if (name != null) {
			return of(
				productService.findByName(name)
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

	@GetMapping("{productId}")
	public CommonApiResponse<ProductResponse> getProduct(Long productId) {
		return of(
			new ProductResponse(
				productService.findOne(productId)
			)
		);
	}

	@PostMapping
	public CommonApiResponse<ProductResponse> producting(@RequestBody ProductRequest productRequest) {
		return of(
			new ProductResponse(
				productService.write(productRequest)
			)
		);
	}
}
