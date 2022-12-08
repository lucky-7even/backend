package com.luckyseven.backend.domain.product.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.luckyseven.backend.domain.member.entity.Member;

import com.luckyseven.backend.domain.product.model.Category;
import com.luckyseven.backend.domain.product.model.ProductStatus;
import com.luckyseven.backend.domain.product.model.Region;
import com.luckyseven.backend.global.config.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
}
