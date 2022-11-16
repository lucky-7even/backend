package com.luckyseven.backend.domain.product;

import static org.springframework.beans.BeanUtils.*;

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

import com.luckyseven.backend.domain.member.Member;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
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
	private Region region = Region.BONGCHEON;

	@Enumerated(EnumType.STRING)
	private ProductStatus productStatus = ProductStatus.WAITING;

	@ElementCollection(fetch = FetchType.LAZY)
	private List<String> images = new ArrayList<>();

	public Product() {
	}

	public Product(ProductRequest source) {
		copyProperties(source, this);
	}
}
