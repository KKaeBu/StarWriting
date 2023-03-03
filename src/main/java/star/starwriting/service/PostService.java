package star.starwriting.service;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import star.starwriting.domain.Member;
import star.starwriting.domain.Post;
import star.starwriting.domain.PostComment;
import star.starwriting.dto.PostCommentRequestDto;
import star.starwriting.dto.PostRequestDto;
import star.starwriting.dto.PostResponseDto;
import star.starwriting.repository.MemberRepository;
import star.starwriting.repository.PostCommentRepository;
import star.starwriting.repository.PostRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostCommentRepository postCommentRepository;
    private final JwtProvider jwtProvider;

    @Autowired
    public PostService(PostRepository postRepository, MemberRepository memberRepository,PostCommentRepository postCommentRepository, JwtProvider jwtProvider) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
        this.postCommentRepository = postCommentRepository;
    }

    /* post 작성 함수 */
    public boolean post(PostRequestDto postRequestDto,String token){
        boolean claims = jwtProvider.parseJwtToken(token);
        System.out.println("토큰 진위여부: "+claims);

        if(claims){
            String memberId = postRequestDto.getMember();
            Member member = memberRepository.findByMemberId(memberId).get();

            Post post = postRequestDto.toEntity(member);
            postRepository.save(post);

            return true;
        }else{
            return false;
        }
    }

    public boolean comment(PostCommentRequestDto postCommentRequestDto, String token){
        boolean claims = jwtProvider.parseJwtToken(token);
        System.out.println("토큰 진위여부: "+claims);

        if(claims){
            String memberId = postCommentRequestDto.getMember();
            Member member = memberRepository.findByMemberId(memberId).get();
            Long postId = postCommentRequestDto.getPost();
            Post post = postRepository.findById(postId).get();

            PostComment postComment = postCommentRequestDto.toEntity(member,post);

            // 현재 날짜 구하기
            LocalDateTime now = LocalDateTime.now();
            // 포맷 정의
            String formatDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            // 포맷 적용
            postComment.setCommentDate(formatDate);

            postCommentRepository.save(postComment);

            return true;
        }else{
            return false;
        }
    }

    /* 모든 post 조회 */
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
