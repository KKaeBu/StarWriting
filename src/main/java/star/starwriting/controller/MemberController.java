package star.starwriting.controller;

import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import star.starwriting.domain.Member;
import star.starwriting.domain.Post;
import star.starwriting.dto.*;
import star.starwriting.service.ImageStore;
import star.starwriting.service.MemberService;
import star.starwriting.service.PostService;

import javax.print.attribute.standard.Media;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

@Controller
public class MemberController {

    private final MemberService memberService;
    private final PostService postService;
    private final ImageStore imageStore;
    private final ResourceLoader resourceLoader;


    @Autowired
    public MemberController(MemberService memberService, PostService postService, ImageStore imageStore, ResourceLoader resourceLoader) {
        this.memberService = memberService;
        this.postService = postService;
        this.imageStore = imageStore;
        this.resourceLoader = resourceLoader;
    }
    //    홈 화면
    @GetMapping(value = {"", "/api", "/", "/api/members"})
    @ResponseBody
    public List<MemberResponseDto> home(Model model) {
        model.addAttribute("members", memberService.findAllMembers());
        return memberService.findAllMembers();
    }

    //    회원 정보 화면
    @GetMapping("/api/members/{id}")
    @ResponseBody
    public MemberResponseDto getMember(@PathVariable("id") Long id) {
        MemberResponseDto member = memberService.findMember(id).get();
        return member;
    }

    //    회원 가입
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
//    @PostMapping("/api/login")
//    public ResponseEntity<HttpHeaders> Login(@RequestBody LoginRequestDto loginRequestDto) {
//        String token = memberService.Login(loginRequestDto.getMemberId(), loginRequestDto.getPassword());
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//
//        if (token != null) {
//            httpHeaders.add("Authorization", "Bearer " + token);
//            return new ResponseEntity<>(httpHeaders, HttpStatus.OK); /* http state code `200` 반환 */
//        } else {
//            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST); /* http state code 400 반환 */
//        }
//    }

    // 로그인 화면
    @GetMapping("/api/login")
    public String Login() {
        return "members/signInForm";
    }

    // 로그인 (폼 이용 버전)
    @PostMapping("/api/login")
    public String Login(LoginRequestDto loginRequestDto, Model model) {
        String token = memberService.Login(loginRequestDto.getMemberId(), loginRequestDto.getPassword());
        String memberId = loginRequestDto.getMemberId();

        HttpHeaders httpHeaders = new HttpHeaders();

        if (token != null) {
            httpHeaders.add("Authorization", "Bearer " + token);
            model.addAttribute("token", token);
            model.addAttribute("posts", postService.findMemberAllPosts(memberId));
            model.addAttribute("member", memberService.findMemberByMemberId(memberId).get());
            return "members/memberHome"; /* http state code `200` 반환 */
        } else {
            return "members/signInForm"; /* http state code 400 반환 */
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

    // 이미지 파일 전달
    @GetMapping(value = "/api/members/{id}/profile",produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] GetProfileImage(@PathVariable Long id) throws IOException {
        MemberResponseDto member = memberService.findMember(id).get();
        String fileName = member.getProfileImage().getStoreFileName();
        String imgRootPath = "static/img/profileImg";

        if(!fileName.equals("basicProfile.png"))
            imgRootPath = "static/members/" + member.getMemberId() + "/profileImg";

        String filePath = imageStore.pathSeperator(imgRootPath) + fileName;
        System.out.println("filePath: " + filePath);

        // getResourceAsStream()의 기본 path가 resources부터 시작임
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(filePath);
        System.out.println(in);
        return IOUtils.toByteArray(in);
    }
}
