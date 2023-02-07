package star.starwriting.service;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import star.starwriting.domain.Member;
import star.starwriting.domain.Post;
import star.starwriting.dto.PostRequestDto;
import star.starwriting.dto.PostResponseDto;
import star.starwriting.repository.MemberRepository;
import star.starwriting.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Autowired
    public PostService(PostRepository postRepository, MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }
    public String post(PostRequestDto postRequestDto,String token){
        System.out.println("토큰: "+token);
        boolean claims = jwtProvider.parseJwtToken(token);
        System.out.println("토큰 진위여부: "+claims);

        if(claims){
            String memberId = postRequestDto.getMember();
            Member member = memberRepository.findByMemberId(memberId).get();

            Post post = postRequestDto.toEntity(member);
            postRepository.save(post);

            return "200";
        }else{
            return "400";
        }
    }

    public List<PostResponseDto> findAllPosts() {
        List<Post> postList = postRepository.findAll();
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        for (Post p : postList) {
            postResponseDtoList
                    .add(new PostResponseDto(
                            p.getId(), p.getPostingDate(), p.getSharedNum(), p.getTitle(), p.getView(), p.getMember()
                        )
                    );
        }
        return postResponseDtoList;
    }
}
