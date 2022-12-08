package com.luckyseven.backend.domain.product.dto;

import static org.springframework.beans.BeanUtils.*;

import com.luckyseven.backend.domain.product.entity.Product;
import com.luckyseven.backend.domain.product.model.Category;
import com.luckyseven.backend.domain.product.model.ProductStatus;
import com.luckyseven.backend.domain.product.model.Region;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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
				.build();
	}
}
