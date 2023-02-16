package star.starwriting.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import star.starwriting.domain.Member;
import star.starwriting.dto.LoginRequestDto;
import star.starwriting.dto.MemberRequestDto;
import star.starwriting.dto.MemberResponseDto;
import star.starwriting.service.JwtProvider;
import star.starwriting.service.MemberService;
import star.starwriting.service.PostService;

import javax.persistence.EntityManager;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class JpaMemberRepositoryTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired PostRepository postRepository;
    @Autowired PostService postService;
    @Autowired JwtProvider jwtProvider;

    @Test
    public void 회원가입과로그인() throws ParseException, IOException {
        //given
        //회원가입
        MemberRequestDto member1 = new MemberRequestDto();

        String dateStr = "2022-05-05";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date birthday = formatter.parse(dateStr);

        member1.setName("한승수");
        member1.setMemberId("hss123");
        member1.setBirthday(birthday);
        member1.setPassword("0000");
        member1.setEmail("hss123@gmail.com");
        member1.setPhoneNum("010-0100-0100");
        member1.setNickname("한승");
        member1.setSex("남");
        //when
        Long savedId = memberService.join(member1,null);

        //then
        MemberResponseDto findMember = memberService.findMember(savedId).get();
        assertThat(member1.getName()).isEqualTo(findMember.getName());

        //로그인
        String loginMemberId = "hss123";
        String loginPassword = "0000";
        LoginRequestDto loginRequestDto = new LoginRequestDto(loginMemberId,loginPassword);

        System.out.println("토큰: "+memberService.Login(loginRequestDto.getMemberId(), loginRequestDto.getPassword()));
    }

    @Test
    public void 로그인()throws ParseException{
        //given
        String memberId = "a";
        String password = "a";

        memberService.Login(memberId, password);
    }

    @Test
    public void 중복_회원_예외() throws ParseException, IOException {
        //given
        MemberRequestDto member1 = new MemberRequestDto();

        String dateStr = "2022-05-05";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date birthday = formatter.parse(dateStr);

        member1.setName("한승수");
        member1.setMemberId("hss123");
        member1.setBirthday(birthday);
        member1.setPassword("0000");
        member1.setEmail("hss123@gmail.com");
        member1.setPhoneNum("010-0100-0100");
        member1.setNickname("한승");
        member1.setSex("남");
// ========= 중복 확인용 같은 사람 한번 더 추가
        MemberRequestDto member2 = new MemberRequestDto();

        member2.setName("한승수");
        member2.setMemberId("hss123");
        member2.setBirthday(birthday);
        member2.setPassword("0000");
        member2.setEmail("hss123@gmail.com");
        member2.setPhoneNum("010-0100-0100");
        member2.setNickname("한승");
        member2.setSex("남");

        //when
        memberService.join(member1,null);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2,null));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }



}