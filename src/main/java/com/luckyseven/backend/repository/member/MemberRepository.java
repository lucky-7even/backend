package com.luckyseven.backend.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luckyseven.backend.domain.Member;
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
