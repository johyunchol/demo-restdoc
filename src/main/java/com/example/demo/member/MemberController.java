package com.example.demo.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * @title 회원 조회
     * @param request
     * @return
     */
    @PostMapping("/{id}")
    public ResponseEntity<MemberResponseDto> getMember(@RequestBody MemberRequestDto request) {
        MemberResponseDto member = memberService.getMemberById(request.getId());
        return ResponseEntity.ok(member);
    }
}
