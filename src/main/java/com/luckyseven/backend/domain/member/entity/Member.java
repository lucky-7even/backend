package com.luckyseven.backend.domain.member.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.luckyseven.backend.domain.product.entity.ProductLikes;
import com.luckyseven.backend.domain.product_demand.entity.ProductDemand;
import com.luckyseven.backend.domain.product_demand.entity.ProductDemandLikes;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String nickname;

    private String profileImage;

    private String email;

    private String password;

    // 동네
    private String location;

    private boolean isSocial;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<ProductDemand> productDemandList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<ProductDemandLikes> productDemandLikesList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<ProductLikes> productLikesList;

    @Builder
    public Member(Long memberId, String nickname, String profileImage,
                  String email, String password, String location,
                  boolean isSocial, List<ProductDemand> productDemandList,
                  List<ProductDemandLikes> productDemandLikesList,
                  List<ProductLikes> productLikesList) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.email = email;
        this.password = password;
        this.location = location;
        this.isSocial = isSocial;
        this.productDemandList = productDemandList;
        this.productDemandLikesList = productDemandLikesList;
        this.productLikesList = productLikesList;
    }
}
