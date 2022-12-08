package com.luckyseven.backend.domain.product;

import static com.luckyseven.backend.global.error.ErrorCode.*;

import java.util.List;
import java.util.stream.Collectors;

import com.luckyseven.backend.domain.member.MemberRepository;
import com.luckyseven.backend.domain.member.entity.Member;
import com.luckyseven.backend.domain.product.dto.ProductRequest;
import com.luckyseven.backend.domain.product.dto.ProductResponse;
import com.luckyseven.backend.domain.product.entity.Product;
import com.luckyseven.backend.domain.product.model.Category;
import com.luckyseven.backend.domain.product.model.ProductStatus;
import com.luckyseven.backend.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;
	private final MemberRepository memberRepository;

	// 물품 등록
	@Transactional
	public ProductResponse makeProduct(String email, ProductRequest productRequest) {
		Member member = memberRepository.findByEmail(email)
				.orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));

		Product product = Product.builder()
				.category(productRequest.getCategory())
				.name(productRequest.getName())
				.price(productRequest.getPrice())
				.description(productRequest.getDescription())
				.productStatus(ProductStatus.WAITING)
				.member(member)
				.build();
		productRepository.save(product);

		return ProductResponse.of(product);
	}

	// 물품 상세 조회
	@Transactional(readOnly = true)
	public ProductResponse findProduct(Long productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new BadRequestException(PRODUCT_NOT_FOUND));
		return ProductResponse.of(product);
	}

	// 물품 전체 조회
	@Transactional(readOnly = true)
	public List<ProductResponse> findProducts(Long productId, Pageable pageable) {
		Page<Product> products = productRepository.findByProductIdLessThanOrderByProductIdDesc(productId, pageable);
		return products.stream()
				.map(ProductResponse::new)
				.collect(Collectors.toList());
	}

	// 물품 검색
	@Transactional(readOnly = true)
	public List<ProductResponse> searchProducts(Long productId, String name, Pageable pageable) {
		Page<Product> products = productRepository.findByProductIdLessThanAndNameContainsOrderByProductIdDesc(productId, name, pageable);
		return products.stream()
				.map(ProductResponse::new)
				.collect(Collectors.toList());
	}

	// 물품 카테고리 조회
	@Transactional(readOnly = true)
	public List<ProductResponse> findCategoryProducts(Long productId, Category category, Pageable pageable) {
		Page<Product> products = productRepository.findByProductIdLessThanAndCategoryOrderByProductIdDesc(productId, category, pageable);
		return products.stream()
				.map(ProductResponse::new)
				.collect(Collectors.toList());
	}
}