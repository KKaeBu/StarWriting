package star.starwriting.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import star.starwriting.dto.PostRequestDto;
import star.starwriting.dto.PostResponseDto;
import star.starwriting.service.MemberService;
import star.starwriting.service.PostService;

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
        PostRequestDto post = new PostRequestDto();

        String title = "테스트코드 제목";
        String memberId = "oooo";

        post.setTitle(title);
        post.setMember(memberId);

        Long savedId = postService.join(post);
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
