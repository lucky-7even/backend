package com.luckyseven.backend.domain.member.entity;

import com.luckyseven.backend.domain.product_demand.entity.ProductDemand;
import com.luckyseven.backend.domain.product_demand.entity.ProductDemandLikes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    // 소셜 회원가입여부
    private boolean isSocial;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<ProductDemand> productDemandList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<ProductDemandLikes> productDemandLikesList;
}
