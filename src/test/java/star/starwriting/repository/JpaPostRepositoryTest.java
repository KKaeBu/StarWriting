package star.starwriting.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import star.starwriting.dto.LoginRequestDto;
import star.starwriting.dto.PostRequestDto;
import star.starwriting.dto.PostResponseDto;
import star.starwriting.service.MemberService;
import star.starwriting.service.PostService;

import java.text.ParseException;
import java.util.List;

@SpringBootTest
@Transactional
public class JpaPostRepositoryTest {
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired PostRepository postRepository;
    @Autowired PostService postService;

    @Test
    public void 글작성() {
        //given
        //로그인 - 미리 만들어둔 테스트용 멤버
        String loginMemberId = "a";
        String loginPassword = "a";
        LoginRequestDto loginRequestDto = new LoginRequestDto(loginMemberId,loginPassword);

        String testToken = "Bearer "+memberService.Login(loginRequestDto.getMemberId(), loginRequestDto.getPassword());

        PostRequestDto post = new PostRequestDto();

        String title = "테스트코드 제목";
        String mainText = "테스트코드 글 내용";
        String memberId = "a";

        post.setTitle(title);
        post.setMember(memberId);
        post.setMainText(mainText);

        if(postService.post(post,testToken)){
            System.out.println("글 작성 테스트 성공");
        }else{
            System.out.println("글 작성 테스트 실패");
        }
    }

    @Test
    public void 모든글제목불러오기() {
        //given
        //when
        //then
        List<PostResponseDto> allPosts = postService.findAllPosts();
        for (PostResponseDto post : allPosts) {
            System.out.println(post.getTitle());
        }
    }
}
