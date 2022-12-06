package com.luckyseven.backend.domain.chatmessage.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class ChatMessageRequest {

	@NotBlank(message = "내용은 필수 값입니다.")
	private String content;

	@NotNull(message = "멤버 ID는 필수 값입니다.")
	private Long memberId;

	@NotBlank(message = "채팅방 ID는 필수 값입니다.")
	private String chatRoomId;

}
