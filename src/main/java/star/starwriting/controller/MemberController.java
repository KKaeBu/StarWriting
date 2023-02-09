package star.starwriting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import star.starwriting.domain.Member;
import star.starwriting.dto.LoginRequestDto;
import star.starwriting.dto.MemberRequestDto;
import star.starwriting.dto.MemberResponseDto;
import star.starwriting.service.MemberService;

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

    @PostMapping("/api/signup")
    public Long SignUp(@RequestBody MemberRequestDto requestDto) {
        return memberService.join(requestDto);
    }
}
