package com.luckyseven.backend.domain.product;

import static org.springframework.beans.BeanUtils.*;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductResponse {

	@ApiModelProperty(value = "PK", required = true)
	private Long productId;

	@ApiModelProperty(value = "대여하는 유저", required = true)
	private Long memberId;

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

	public ProductResponse(Product source) {
		copyProperties(source, this);
	}
}