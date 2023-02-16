package star.starwriting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import star.starwriting.domain.Like;
import star.starwriting.domain.Member;
import star.starwriting.domain.Post;
import star.starwriting.dto.LikeRequestDto;
import star.starwriting.repository.LikeRepository;
import star.starwriting.repository.MemberRepository;
import star.starwriting.repository.PostRepository;

import java.util.Optional;

@Service
@Transactional
public class LikeService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;
    private final JwtProvider jwtProvider;

    @Autowired
    public LikeService(PostRepository postRepository, MemberRepository memberRepository, LikeRepository likeRepository, JwtProvider jwtProvider) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
        this.likeRepository = likeRepository;
        this.jwtProvider = jwtProvider;
    }

    public boolean like(LikeRequestDto likeRequestDto, String token){
        boolean claims = jwtProvider.parseJwtToken(token);
        System.out.println("토큰 진위여부: "+claims);

        if(claims){
            String memberId = likeRequestDto.getMember();
            Long postId = likeRequestDto.getPost();

            Member member = memberRepository.findByMemberId(memberId).get();
            Post post = postRepository.findById(postId).get();

            Like like = likeRequestDto.toEntity(member,post);
            likeRepository.save(like);

            return true;
        }else{
            return false;
        }


    }
}
