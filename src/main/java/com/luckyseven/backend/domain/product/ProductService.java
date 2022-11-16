package com.luckyseven.backend.domain.product;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckyseven.backend.global.error.exception.NotFoundException;
import com.luckyseven.backend.domain.member.MemberService;

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
		// Member member = memberService.findOne(productRequest.getMemberId());
		Product product = new Product(productRequest);
		System.out.println(product);
		// member의 위도 경도로 동 찾기
		return productRepository.save(product);
	}

	public List<Product> findAll() {
		return productRepository.findAll();
	}

	public Product findOne(Long id) {
		return productRepository.findById(id)
			.orElseThrow(() -> new NotFoundException("해당 Product가 존재하지 않습니다."));
	}

	public List<Product> findByNameContains(String name) {
		return productRepository.findByNameContains(name);
	}
}
