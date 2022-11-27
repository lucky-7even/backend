package com.luckyseven.backend.domain.product;

import static com.luckyseven.backend.global.error.ErrorCode.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckyseven.backend.domain.member.MemberService;
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

	public Page<Product> findByIdLessThanOrderByIdDesc(Long id, PageRequest pageRequest) {
		return productRepository.findByIdLessThanOrderByIdDesc(id, pageRequest);
	}

	public Product findOne(Long id) {
		return productRepository.findById(id)
			.orElseThrow(() -> new BusinessException(PRODUCT_NOT_FOUND));
	}

	public List<Product> findByIdLessThanAndNameContainsOrderByIdDesc(Long id, String name, PageRequest pageRequest) {
		return productRepository.findByIdLessThanAndNameContainsOrderByIdDesc(id, name, pageRequest);
	}

	public Page<Product> findByIdLessThanAndCategoryOrderByIdDesc(Long id, Category category, PageRequest pageRequest) {
		return productRepository.findByIdLessThanAndCategoryOrderByIdDesc(id, category, pageRequest);
	}
}