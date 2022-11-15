package com.luckyseven.backend.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;

@Entity
@Getter
public class Product {
	@Id @GeneratedValue
	@Column(name = "product_id")
	private Long productId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Enumerated(EnumType.STRING)
	private Category category;

	private String name;

	private int price;

	private String description;

	@Enumerated(EnumType.STRING)
	private Region region;

	@Enumerated(EnumType.STRING)
	private ProductStatus productStatus;

	@ElementCollection(fetch = FetchType.LAZY)
	private List<String> images = new ArrayList<>();

	public Product() {
	}

	public Product(Member member, String name, int price, String description) {
		this.member = member;
		this.name = name;
		this.price = price;
		this.description = description;
	}
}
