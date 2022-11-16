package com.luckyseven.backend.domain.member;

import org.springframework.stereotype.Service;

import com.luckyseven.backend.global.error.ErrorCode;
import com.luckyseven.backend.global.error.exception.BusinessException;

@Service
public class MemberService {

	private final MemberRepository memberRepository;

	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	public Member findOne(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
	}
}
