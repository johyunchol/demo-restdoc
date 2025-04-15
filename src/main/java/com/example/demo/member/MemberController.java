package com.example.demo.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * @title 회원 조회
     * @description 회원 ID로 회원 정보를 조회합니다.
     */
    @PostMapping("/{id}")
    public ResponseEntity<MemberResponseDto> getMember(@RequestBody MemberRequestDto request) {
        MemberResponseDto member = memberService.getMemberById(request.getId());
        return ResponseEntity.ok(member);
    }
}
