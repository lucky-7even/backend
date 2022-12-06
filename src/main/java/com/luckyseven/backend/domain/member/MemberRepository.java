package com.luckyseven.backend.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luckyseven.backend.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
