package com.luckyseven.backend.domain.product;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.luckyseven.backend.domain.member.Member;

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

	@NotNull
	@Enumerated(EnumType.STRING)
	private Category category;

	@NotNull
	@Size(max = 50)
	private String name;

	@NotNull
	private int price;

	@NotNull
	private String description;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Region region = Region.BONGCHEON;

	@Enumerated(EnumType.STRING)
	private ProductStatus productStatus = ProductStatus.WAITING;

	@ElementCollection(fetch = FetchType.LAZY)
	private List<String> images = new ArrayList<>();

	public Product() {
	}

	public Product(Member member, Category category, String name, int price, String description) {
		this.member = member;
		this.category = category;
		this.name = name;
		this.price = price;
		this.description = description;
	}
}
