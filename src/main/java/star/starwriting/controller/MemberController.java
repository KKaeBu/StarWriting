package star.starwriting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import star.starwriting.domain.Member;
import star.starwriting.dto.LoginRequestDto;
import star.starwriting.dto.MemberRequestDto;
import star.starwriting.dto.MemberResponseDto;
import star.starwriting.service.MemberService;

import java.net.http.HttpHeaders;
import java.util.List;
import java.util.Optional;

@RestController
public class MemberController {

    private final MemberService memberService;
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/api/members")
    public List<MemberResponseDto> getMemberList() {
        return memberService.findAllMembers();
    }

    @GetMapping("/api/members/{id}")
    public Optional<MemberResponseDto> getMember(@PathVariable("id") Long memberId) {
        return memberService.findMember(memberId);
    }

    @PostMapping("/api/signup")
    public Long SignUp(@RequestBody MemberRequestDto requestDto) {
        return memberService.join(requestDto);
    }

    @PostMapping("/api/login")
    public String Login(@RequestBody LoginRequestDto loginRequestDto) {
        String token = memberService.Login(loginRequestDto.getMemberId(),loginRequestDto.getPassword());
        return token;
    }
}
