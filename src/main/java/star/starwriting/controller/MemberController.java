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
import star.starwriting.domain.Post;
import star.starwriting.dto.*;
import star.starwriting.service.ImageStore;
import star.starwriting.service.MemberService;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
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
    //    리액트 연결 테스트
    @GetMapping("/api/react")
    @ResponseBody
    public String test(){
        return "Now, Connected";
    }

    //    홈 화면
    @GetMapping(value = { "/api/members"})
    @ResponseBody
    public List<MemberResponseDto> home(Model model) {
        model.addAttribute("members", memberService.findAllMembers());
        return memberService.findAllMembers();
    }

//    //    회원 목록 조회 화면
//    @GetMapping("/api/members")
//    public String getMemberList() {
//        return "members/signUpForm";
//    }

    //    회원 정보 화면
    @GetMapping(value = {"/api/members/{Id}"})
    @ResponseBody
    public MemberResponseDto getMember(@PathVariable("Id") Long id, Model model) {
        MemberResponseDto member = memberService.findMember(id).get();
        model.addAttribute("member", member);
        return member;
    }

    //    회원 가입 화면
    @PostMapping("/api/members")
    public String signup(MemberRequestDto requestDto, @RequestParam MultipartFile file) throws IOException {
        memberService.join(requestDto, file);
        return "members/signUpForm";
    }

    @PostMapping("/api/like")
    @ResponseBody
    public ResponseEntity<String> like(@RequestBody LikeRequestDto likeRequestDto) {
        memberService.like(likeRequestDto);
        return new ResponseEntity<>("좋아요 성공", HttpStatus.CREATED);
    }

    @GetMapping("/api/like")
    @ResponseBody
    public ResponseEntity<List<Post>> likeList(@RequestBody LikeRequestDto likeRequestDto) {
        List<Post> likePosts = memberService.likeList(likeRequestDto);
        return new ResponseEntity<List<Post>>(likePosts, HttpStatus.ACCEPTED);
    }

    // 로그인
    @PostMapping("/api/login")
    public ResponseEntity<HttpHeaders> Login(@RequestBody LoginRequestDto loginRequestDto) {
        String token = memberService.Login(loginRequestDto.getMemberId(), loginRequestDto.getPassword());

        HttpHeaders httpHeaders = new HttpHeaders();

        if (token != null) {
            httpHeaders.add("Authorization", "Bearer " + token);
            return new ResponseEntity<>(httpHeaders, HttpStatus.OK); /* http state code `200` 반환 */
        } else {
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST); /* http state code 400 반환 */
        }
    }

    @PostMapping("/api/follow")
    public ResponseEntity<List<Member>> Follow(@RequestBody FollowRequestDto followRequestDto) {
        List<Member> followingMember = memberService.follow(followRequestDto);

        if (!followingMember.isEmpty()) {
            return new ResponseEntity<List<Member>>(followingMember, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<List<Member>>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/api/follow")
    @ResponseBody
    public String GetFollow(@RequestBody FollowRequestDto followRequestDto) {
        memberService.getFollow(followRequestDto);
        return "성공";
    }
}
