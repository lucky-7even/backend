package com.luckyseven.backend.domain.member;

import com.luckyseven.backend.domain.member.dto.MemberRequestDto;
import com.luckyseven.backend.domain.member.dto.MemberResponseDto;
import com.luckyseven.backend.domain.member.entity.Member;
import com.luckyseven.backend.global.error.ErrorCode;
import com.luckyseven.backend.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    // 일반 회원가입
    @Transactional
    public MemberResponseDto makeMember(MemberRequestDto memberRequestDto) {
        Optional<Member> checkMember = memberRepository.findByEmail(memberRequestDto.getEmail());
        if (checkMember.isPresent()) {
            throw new BadRequestException(ErrorCode.MEMBER_ALREADY_EXIST);
        }
        else if (!Objects.equals(memberRequestDto.getPassword(), memberRequestDto.getPasswordConfirm())) {
            throw new BadRequestException(ErrorCode.PASSWORD_CONFIRM_NOT_VALID);
        }

        Member member = memberRequestDto.toMember(memberRequestDto);
        memberRepository.save(member);

        return MemberResponseDto.of(member);
    }
    
    // 일반 로그인
}
