package com.luckyseven.backend.service.member;

import org.springframework.stereotype.Service;

import com.luckyseven.backend.domain.Member;
import com.luckyseven.backend.global.error.exception.NotFoundException;
import com.luckyseven.backend.repository.member.MemberRepository;

@Service
public class MemberService {

	private final MemberRepository memberRepository;

	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	public Member findOne(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(() -> new NotFoundException("Test"));
	}
}
