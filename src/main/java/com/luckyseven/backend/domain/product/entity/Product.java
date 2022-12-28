package com.luckyseven.backend.domain.product.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.luckyseven.backend.domain.member.entity.Member;

import com.luckyseven.backend.domain.product.model.Category;
import com.luckyseven.backend.domain.product.model.ProductStatus;
import com.luckyseven.backend.domain.product.model.Region;
import com.luckyseven.backend.global.config.entity.BaseTimeEntity;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.REMOVE)
	private List<ProductLikes> productLikesList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.REMOVE)
	private List<ProductReply> productReplyList;

	@Builder
	public Product(Long productId, Category category, String name,
				   int price, String description, Region region,
				   ProductStatus productStatus, List<String> images, Member member,
				   List<ProductLikes> productLikesList, List<ProductReply> productReplyList) {
		this.productId = productId;
		this.category = category;
		this.name = name;
		this.price = price;
		this.description = description;
		this.region = region;
		this.productStatus = productStatus;
		this.images = images;
		this.member = member;
		this.productLikesList = productLikesList;
		this.productReplyList = productReplyList;
	}
}
