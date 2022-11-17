package com.luckyseven.backend.domain.member;

import com.luckyseven.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmailAndIsSocialFalse(String email);

    Optional<Member> findByEmailAndIsSocialTrue(String email);

    boolean existsByEmailAndIsSocialFalse(String email);

    boolean existsByEmailAndIsSocialTrue(String email);

    boolean existsByEmail(String email);
}
