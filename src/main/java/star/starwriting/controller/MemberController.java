package star.starwriting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import star.starwriting.domain.Member;
import star.starwriting.domain.MemberProfileImage;
import star.starwriting.dto.MemberProfileImageDto;
import star.starwriting.dto.LoginRequestDto;
import star.starwriting.dto.MemberRequestDto;
import star.starwriting.dto.MemberResponseDto;
import star.starwriting.service.ImageStore;
import star.starwriting.service.MemberService;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

@Controller
public class MemberController {

    private final MemberService memberService;
    private final ImageStore imageStore;

    @Autowired
    public MemberController(MemberService memberService, ImageStore imageStore) {
        this.memberService = memberService;
        this.imageStore = imageStore;
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
        return "members/signUpForm";
    }

//    회원 정보 화면
    @GetMapping("/api/members/{id}")
    public String getMember(@PathVariable("id") Long memberId, Model model) {
        MemberResponseDto member = memberService.findMember(memberId).get();
        model.addAttribute("member", member);
        return "members/memberInfo";
    }

//    회원 가입 화면
    @PostMapping("/api/members")
    public String signup(MemberRequestDto requestDto, @RequestParam MultipartFile file)throws IOException {
        memberService.join(requestDto, file);
        return "members/signUpForm";
    }

    // 로그인
    @PostMapping("/api/login")
    public ResponseEntity<HttpHeaders> Login(@RequestBody LoginRequestDto loginRequestDto) {
        String token = memberService.Login(loginRequestDto.getMemberId(),loginRequestDto.getPassword());

        HttpHeaders httpHeaders = new HttpHeaders();

        if(token != null){
            httpHeaders.add("Authorization","Bearer "+token);
            return new ResponseEntity<>(httpHeaders, HttpStatus.OK); /* http state code `200` 반환 */
        }else{
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST); /* http state code 400 반환 */
        }
    }

//    @PostMapping("/api/signup")
//    public Long SignUp(@RequestBody MemberRequestDto requestDto) {
//        return memberService.join(requestDto);
//    }

}
