package star.starwriting.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import star.starwriting.domain.Member;
import star.starwriting.service.MemberService;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class JpaMemberRepositoryTest {

    @Autowired MemberService memberService;

    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() {
        //given
        Member member1 = new Member();
        member1.setName("한승수");

        //when
        Long savedId = memberService.join(member1);

        //then
        Member findMember = memberService.findOne(savedId).get();
        assertThat(member1.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void 중복_회원_예외() {
        //given
        Member member1 = new Member();
        member1.setName("유상훈");

        Member member2 = new Member();
        member2.setName("유상훈");

        //when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));//성공했다면 예외가 터진거임
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

/*        try {
            memberService.join(member2); //예외가 터져야함
            fail("예외가 발생해야 합니다.");
        }catch (IllegalStateException e) {
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
        }*/

        //then

    }


}