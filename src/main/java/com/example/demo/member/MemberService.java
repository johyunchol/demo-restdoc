package com.example.demo.member;

import org.springframework.stereotype.Service;

@Service
public class MemberService {
    public MemberResponseDto getMemberById(Long id) {
        return new MemberResponseDto(id, "조현철" + id, "kkensu@naver.com");
    }
}
