package com.luckyseven.backend.domain.product;

import lombok.Getter;

@Getter
public class ProductRequest {

	private Long memberId;

	private String name;

	private Category category;

	private int price;

	private String description;

	// private String imageS3URL;

	public ProductRequest(Long memberId, String name, Category category, int price, String description) {
		this.memberId = memberId;
		this.name = name;
		this.category = category;
		this.price = price;
		this.description = description;
	}
}
