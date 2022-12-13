package com.luckyseven.backend.domain.product.dto;

import static org.springframework.beans.BeanUtils.*;

import com.luckyseven.backend.domain.product.entity.Product;
import com.luckyseven.backend.domain.product.entity.ProductLikes;
import com.luckyseven.backend.domain.product.entity.ProductReply;
import com.luckyseven.backend.domain.product.model.Category;
import com.luckyseven.backend.domain.product.model.ProductStatus;
import com.luckyseven.backend.domain.product.model.Region;
import com.luckyseven.backend.domain.product.repository.ProductLikesRepository;
import com.luckyseven.backend.domain.product.repository.ProductReplyRepository;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {

	@ApiModelProperty(value = "PK", required = true)
	private Long productId;

	@ApiModelProperty(value = "카테고리", required = true)
	private Category category;

	@ApiModelProperty(value = "이름", required = true)
	private String name;

	@ApiModelProperty(value = "가격", required = true)
	private int price;

	@ApiModelProperty(value = "설명", required = true)
	private String description;

	@ApiModelProperty(value = "상태", required = true)
	private ProductStatus productStatus;

	@ApiModelProperty(value = "지역", required = true)
	private Region region;

	@ApiModelProperty(value = "물건 등록한 사람", required = true)
	private String productRegistrant;

	@ApiModelProperty(value = "물건 이미지들", required = true)
	private List<String> productImages;

	@ApiModelProperty(value = "물품 찜 수", required = true)
	private Long likes;

	@ApiModelProperty(value = "댓글 수", required = true)
	private Long replies;

	@ApiModelProperty(value = "물품 댓글", required = true)
	private List<ProductReplyResponseDto> productReplyResponseDtoList;

	public ProductResponse(Product source) {
		copyProperties(source, this);
	}

	public static ProductResponse of(Product product) {
		return ProductResponse.builder()
				.productId(product.getProductId())
				.category(product.getCategory())
				.name(product.getName())
				.price(product.getPrice())
				.description(product.getDescription())
				.productStatus(product.getProductStatus())
				.region(product.getRegion())
				.productRegistrant(product.getMember().getNickname())
				.productImages(product.getImages())
				.productReplyResponseDtoList(product.getProductReplyList().stream()
						.sorted(Comparator.comparing(ProductReply::getCreatedAt).reversed())
						.map(ProductReplyResponseDto::of)
						.collect(Collectors.toList()))
				.likes(product.getProductLikesList().stream()
						.filter(likes -> likes.getProduct().getProductId().equals(product.getProductId()) && likes.isLike())
						.count())
				.replies(product.getProductReplyList().stream()
						.filter(replies -> replies.getProduct().getProductId().equals(product.getProductId()))
						.count())
				.build();
	}
}
