package star.starwriting.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import star.starwriting.domain.Member;
import star.starwriting.dto.MemberRequestDto;
import star.starwriting.dto.MemberResponseDto;
import star.starwriting.service.MemberService;

import java.util.List;
import java.util.Optional;

@Controller
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

//    회원 목록 조회 화면
    @GetMapping(value = {"", "/"})
    public String home(Model model) {
        model.addAttribute("members", memberService.findAllMembers());
        return "home";
    }

//    회원 가입 화면
    @GetMapping("/api/members")
    public String getMemberList() {
//        return memberService.findAllMembers();
        return "members/signUpForm";
    }

    @GetMapping("/api/members/{id}")
    public Optional<MemberResponseDto> getMember(@PathVariable("id") Long memberId) {
        return memberService.findMember(memberId);
    }

    @PostMapping("/api/members")
    public String saveMember(MemberRequestDto requestDto) {
//        return memberService.join(requestDto);
        memberService.join(requestDto);
        return "redirect:/";
    }
}
