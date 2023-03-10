package star.starwriting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import star.starwriting.domain.Member;
import star.starwriting.domain.Post;
import star.starwriting.domain.PostComment;
import star.starwriting.dto.MemberResponseDto;
import star.starwriting.dto.PostCommentRequestDto;
import star.starwriting.dto.PostRequestDto;
import star.starwriting.dto.PostResponseDto;
import star.starwriting.repository.MemberRepository;
import star.starwriting.repository.PostCommentRepository;
import star.starwriting.repository.PostRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final DirManager dirManager;
    private final ImageStore imageStore;
    private final PostCommentRepository postCommentRepository;
    private final JwtProvider jwtProvider;

    @Autowired
    public PostService(PostRepository postRepository, MemberRepository memberRepository, DirManager dirManager, ImageStore imageStore, PostCommentRepository postCommentRepository, JwtProvider jwtProvider) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
        this.dirManager = dirManager;
        this.imageStore = imageStore;
        this.jwtProvider = jwtProvider;
        this.postCommentRepository = postCommentRepository;
    }

    /* post 작성 함수 */
    public boolean post(PostRequestDto postRequestDto, MultipartFile file) throws IOException {
            String memberId = postRequestDto.getMember();
            Member member = memberRepository.findByMemberId(memberId).get();

            Post post = postRequestDto.toEntity(member);
            dirManager.createPostDir(member.getMemberId(), post); // 포스팅 폴더 생성
            imageStore.storePostImage(member.getMemberId(), post, file); // 포스팅에 사용된 이미지 저장 (미완)
            postRepository.save(post);
            return true;
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

    public boolean isValidToken(String token){
        return jwtProvider.parseJwtToken(token);
    }

    /* 모든 post 조회 */
    public List<PostResponseDto> findAllPosts() {
        List<Post> postList = postRepository.findAll();
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        for (Post p : postList) {
            postResponseDtoList
                    .add(new PostResponseDto(
                            p.getId(),
                            p.getPostingDate(),
                            p.getSharedNum(),
                            p.getTitle(),
                            p.getView(),
                            p.getMember(),
                            p.getPostImage()
                        )
                    );
        }
        return postResponseDtoList;
    }

    // 특정 글 검색
    public Optional<PostResponseDto> findPost(Long postId) {
        Post post = postRepository.findById(postId).get();

        PostResponseDto postResponseDto = new PostResponseDto(
                post.getId(),
                post.getPostingDate(),
                post.getSharedNum(),
                post.getTitle(),
                post.getView(),
                post.getMember(),
                post.getPostImage()
        );

        return Optional.ofNullable(postResponseDto);
    }

    // 특정 회원이 작성한 모든 글
    public List<PostResponseDto> findMemberAllPosts(String memberId) {
        List<Post> postList = postRepository.findMemberAll(memberId);
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        for (Post p : postList) {
            postResponseDtoList
                    .add(new PostResponseDto(
                                    p.getId(),
                                    p.getPostingDate(),
                                    p.getSharedNum(),
                                    p.getTitle(),
                                    p.getView(),
                                    p.getMember(),
                                    p.getPostImage()
                            )
                    );
        }
        return postResponseDtoList;
    }
}
