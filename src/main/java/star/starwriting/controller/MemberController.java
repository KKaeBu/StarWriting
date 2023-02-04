package star.starwriting.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import star.starwriting.dto.MemberRequestDto;
import star.starwriting.dto.MemberResponseDto;
import star.starwriting.service.MemberService;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class MemberController {

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    private final MemberService memberService;

    @GetMapping("/api/members")
    public List<MemberResponseDto> getMemberList() {
        return memberService.findAllMembers();
    }

    @GetMapping("/api/members/{id}")
    public Optional<MemberResponseDto> getMember(@PathVariable("id") Long memberId) {
        return memberService.findMember(memberId);
    }

//    @GetMapping("/api/members/google")
//    public Map<String, Object> currentMember(Model model, OAuth2AuthenticationToken oAuth2AuthenticationToken) {
//        Map<String, Object> data = oAuth2AuthenticationToken.getPrincipal().getAttributes();
//
//        try {
//            // [UTC DATE 포맷 형식 지정 실시]
//            String formatTimeZone = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
//
//            // [UTC 형식 Date 값 정의 실시]
//            //String utcDateValue="2022-11-08T05:25:00.000Z";
//            String expiredDate = data.get("exp").toString();
//            String result = expiredDate.substring(0,expiredDate.length()-1);
//            result = result+ ".000Z";
//
//            // [UTC 값을 포맷 형식 지정해 Date 객체로 선언]
//            Date date = new SimpleDateFormat(formatTimeZone).parse(result);
//
//            // [Date to Calendar 변환]
//            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
//            calendar.setTime(date);
//
//            // [한국형 시간은 UTC 시간보다 9시간 앞선다]
//            calendar.add(Calendar.HOUR, 9);
//
//            // [한국형 시간으로 변경된 값 저장 실시]
//            SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String koreaDate = sdformat.format(calendar.getTime());
//            System.out.println("로그인 만료기한: " + koreaDate);
//        }
//        catch (Exception e){
//            System.out.println("에러: "+ e);
//        }
//        return data;
//    }

//    @GetMapping("/api/members/naver")
//    public String GetNaverTest() {
//        String naverUrl = "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=vJM1UI0GHvFFCgqxNqBU&state=STATE_STRING&redirect_uri=http://localhost:8080/login/oauth2/code/naver";
//        RestTemplate restTemplate = new RestTemplate();
//        String response = restTemplate.getForObject(naverUrl, String.class);
//        System.out.println(response);
//        return response;
//    }
//
//    @GetMapping("/login/oauth2/code/naver")
//    public String loginPOSTNaver(HttpSession session) {
//        System.out.println(session);
//        return "callback";
//    }



//    @GetMapping("/api/test/google")
//    public String testPeopleAPI(){
//        RestTemplate restTemplate = new RestTemplate();
//        String accessToken = "AIzaSyCDIPKXqXD5DjwlclMTT0CF2QJy68lhm9Y";
//        String url = "https://people.googleapis.com/v1/people/me?personFields=emailAddresses,names&access_token=" + accessToken;
//        System.out.println(url);
//        String response = restTemplate.getForObject(url, String.class);
//        System.out.println(response);
//        return response;
//    }

    @PostMapping("/api/members")
    public Long saveMember(@RequestBody MemberRequestDto requestDto) {
        return memberService.join(requestDto);
    }
}
