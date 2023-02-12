package star.starwriting.controller;

import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import star.starwriting.domain.Member;
import star.starwriting.domain.MemberProfileImage;
import star.starwriting.dto.MemberProfileImageDto;
import star.starwriting.dto.MemberRequestDto;
import star.starwriting.dto.MemberResponseDto;
import star.starwriting.service.ImageStore;
import star.starwriting.service.MemberService;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

@Controller
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

//    홈 화면
    @GetMapping(value = {"", "/"})
    public String home(Model model) {
        model.addAttribute("members", memberService.findAllMembers());
        return "home";
    }

//    회원 목록 조회 화면
    @GetMapping("/api/members")
    public String getMemberList() {
//        return memberService.findAllMembers();
        return "members/signUpForm";
    }

//    회원 정보 화면
    @GetMapping("/api/members/{id}")
    public String getMember(@PathVariable("id") Long memberId, Model model) {
//        return memberService.findMember(memberId);
        MemberResponseDto member = memberService.findMember(memberId).get();
        model.addAttribute("member", member);
        return "members/memberInfo";
    }

    @ResponseBody
    @GetMapping("/api/img/{filename}")
    public Resource showImage(@PathVariable String filename) throws MalformedURLException {
        ImageStore imageStore = new ImageStore();
        Resource resource = (Resource) new UrlResource("file:" + imageStore.getFullPath(filename));
        return resource;
    }

//    회원 가입 화면
    @PostMapping("/api/members")
    public String saveMember(MemberRequestDto requestDto) {
//        return memberService.join(requestDto);
        memberService.join(requestDto);
        return "members/signUpForm";
    }

}
