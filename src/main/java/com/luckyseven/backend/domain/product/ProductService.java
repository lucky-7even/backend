package com.luckyseven.backend.domain.product;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckyseven.backend.domain.member.MemberService;
import com.luckyseven.backend.global.error.ErrorCode;
import com.luckyseven.backend.global.error.exception.BusinessException;

@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final MemberService memberService;

	public ProductService(ProductRepository productRepository, MemberService memberService) {
		this.productRepository = productRepository;
		this.memberService = memberService;
	}

	@Transactional
	public Product write(ProductRequest productRequest) {
		return productRepository.save(
			new Product(
					memberService.findOne(productRequest.getMemberId()),
					productRequest.getCategory(),
					productRequest.getName(),
					productRequest.getPrice(),
					productRequest.getDescription()
			)
		);
	}

	public List<Product> findAll() {
		return productRepository.findAll();
	}

	public Product findOne(Long id) {
		return productRepository.findById(id)
			.orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
	}

	public List<Product> findByNameContains(String name) {
		return productRepository.findByNameContains(name);
	}
}
