package com.luckyseven.backend.domain.member;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.luckyseven.backend.domain.product_demand.entity.ProductDemand;
import com.luckyseven.backend.domain.product_demand.entity.ProductDemandLikes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

<<<<<<< HEAD:src/main/java/com/luckyseven/backend/domain/member/Member.java
=======
import javax.persistence.*;
import java.util.List;

>>>>>>> feature/buyer:src/main/java/com/luckyseven/backend/domain/member/entity/Member.java
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    private String nickname;

    private String profileImage;

    private String email;

    private String passwd;

<<<<<<< HEAD:src/main/java/com/luckyseven/backend/domain/member/Member.java
    private Double lat;

    private Double lng;
=======
    // 동네
    private String location;
>>>>>>> feature/buyer:src/main/java/com/luckyseven/backend/domain/member/entity/Member.java

    private boolean isSocial;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<ProductDemand> productDemandList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<ProductDemandLikes> productDemandLikesList;
}
