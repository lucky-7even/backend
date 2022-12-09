package com.luckyseven.backend.domain.product.dto;

import com.luckyseven.backend.domain.product.model.Category;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
	@ApiModelProperty(value = "등록물품이름", example = "축구공")
	@Size(max = 50)
	@NotNull
	private String name;

	@ApiModelProperty(value = "카테고리", example = "SPORTS")
	@NotNull
	private Category category;

	@ApiModelProperty(value = "가격", example = "3000")
	@NotNull
	private int price;

	@ApiModelProperty(value = "설명", example = "실제 손흥민이 찼던 공입니다!!")
	@NotNull
	private String description;
}
