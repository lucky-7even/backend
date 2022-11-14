package com.luckyseven.backend.controller.product;

import lombok.Getter;

@Getter
public class ProductRequest {

	private Long memberId;

	private String name;

	private int price;

	private String description;

	// images
}
