package com.luckyseven.backend.domain.product;

import static com.luckyseven.backend.global.error.ErrorCode.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.luckyseven.backend.domain.member.MemberRepository;
import com.luckyseven.backend.domain.member.entity.Member;
import com.luckyseven.backend.domain.product.dto.ProductReplyRequestDto;
import com.luckyseven.backend.domain.product.dto.ProductReplyResponseDto;
import com.luckyseven.backend.domain.product.dto.ProductRequest;
import com.luckyseven.backend.domain.product.dto.ProductResponse;
import com.luckyseven.backend.domain.product.entity.Product;
import com.luckyseven.backend.domain.product.entity.ProductLikes;
import com.luckyseven.backend.domain.product.entity.ProductReply;
import com.luckyseven.backend.domain.product.model.Category;
import com.luckyseven.backend.domain.product.model.ProductStatus;
import com.luckyseven.backend.domain.product.model.SearchCondition;
import com.luckyseven.backend.domain.product.repository.ProductLikesRepository;
import com.luckyseven.backend.domain.product.repository.ProductReplyRepository;
import com.luckyseven.backend.domain.product.repository.ProductRepository;
import com.luckyseven.backend.global.config.s3.AwsS3ServiceImpl;
import com.luckyseven.backend.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;
	private final MemberRepository memberRepository;
	private final ProductLikesRepository productLikesRepository;
	private final ProductReplyRepository productReplyRepository;
	private final AwsS3ServiceImpl awsS3Service;

	// 물품 등록
	@Transactional
	public ProductResponse makeProduct(String email, ProductRequest productRequest, List<MultipartFile> multipartFiles) {
		Member member = memberRepository.findByEmail(email)
				.orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));

		List<String> productImages = awsS3Service.uploadImage(multipartFiles, "product");

		Product product = Product.builder()
				.category(productRequest.getCategory())
				.name(productRequest.getName())
				.price(productRequest.getPrice())
				.description(productRequest.getDescription())
				.productStatus(ProductStatus.WAITING)
				.member(member)
				.images(productImages)
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
	public List<ProductResponse> findProducts(Long productId, int size) {
		Pageable pageable = PageRequest.of(0, size, Sort.by("createdAt").descending());
		Page<Product> products = productRepository.findByProductIdLessThanOrderByProductIdDesc(productId, pageable);
		return products.stream()
				.map(ProductResponse::of)
				.collect(Collectors.toList());
	}

	// 물품 검색
	@Transactional(readOnly = true)
	public List<ProductResponse> searchProducts(Long productId, String name, SearchCondition search, int size) {
		Pageable pageable = PageRequest.of(0, size);
		Page<Product> products = productRepository.findByProductIdLessThanAndNameContainsOrderByProductIdDesc(productId, name, pageable);
		List<ProductResponse> searchProducts = products
				.stream()
				.map(ProductResponse::of)
				.collect(Collectors.toList());

		if (search != null) searchProducts = conditionProducts(products, search);

		return searchProducts;
	}

	// 물품 카테고리 조회
	@Transactional(readOnly = true)
	public List<ProductResponse> findCategoryProducts(Long productId, Category category, SearchCondition search, int size) {
		Pageable pageable = PageRequest.of(0, size);
		Page<Product> products = productRepository.findByProductIdLessThanAndCategoryOrderByProductIdDesc(productId, category, pageable);

		List<ProductResponse> productCategories = products.stream()
				.map(ProductResponse::of)
				.collect(Collectors.toList());

		if (search != null) productCategories = conditionProducts(products, search);

		return productCategories;
	}

	// 물품 찜
	@Transactional
	public void likeProduct(String email, Long productId) {
		Member member = memberRepository.findByEmail(email)
				.orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new BadRequestException(PRODUCT_NOT_FOUND));

		if (!productLikesRepository.existsByMember_Email(email)) {
			ProductLikes productLikes = ProductLikes.builder()
					.isLike(true)
					.product(product)
					.member(member).build();
			productLikesRepository.save(productLikes);
		} else {
			ProductLikes productDemandLikes = productLikesRepository.findByProduct_ProductId(productId);
			if (productDemandLikes.isLike()) productDemandLikes.cancelLike();
			else productDemandLikes.setLike();
		}
	}

	// 물품 댓글 작성(대댓글 없음)
	public ProductReplyResponseDto makeProductReply(String email, Long productId, ProductReplyRequestDto productReplyRequestDto) {
		Member member = memberRepository.findByEmail(email)
				.orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new BadRequestException(PRODUCT_NOT_FOUND));

		productReplyRequestDto.setProduct(product);
		productReplyRequestDto.setMember(member);
		ProductReply productReply = ProductReplyRequestDto.toProductReply(productReplyRequestDto);
		productReplyRepository.save(productReply);

		return ProductReplyResponseDto.of(productReply);
	}

	public List<ProductResponse> conditionProducts(Page<Product> products, SearchCondition condition) {
		List<ProductResponse> productResponses;
		if (condition.equals(SearchCondition.RV)) {
			productResponses = products.stream()
					.map(ProductResponse::of)
					.sorted(Comparator.comparing(ProductResponse::getReplies).reversed())
					.collect(Collectors.toList());
		} else if (condition.equals(SearchCondition.LK)) {
			productResponses = products.stream()
					.map(ProductResponse::of)
					.sorted(Comparator.comparing(ProductResponse::getLikes).reversed())
					.collect(Collectors.toList());
		} else if (condition.equals(SearchCondition.HP)) {
			productResponses = products.stream()
					.map(ProductResponse::of)
					.sorted(Comparator.comparing(ProductResponse::getPrice).reversed())
					.collect(Collectors.toList());
		} else {
			productResponses = products.stream()
					.map(ProductResponse::of)
					.sorted(Comparator.comparing(ProductResponse::getPrice))
					.collect(Collectors.toList());
		}
		return productResponses;
	}
}