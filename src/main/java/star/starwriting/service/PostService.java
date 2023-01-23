package star.starwriting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import star.starwriting.domain.Member;
import star.starwriting.domain.Post;
import star.starwriting.dto.PostRequestDto;
import star.starwriting.repository.MemberRepository;
import star.starwriting.repository.PostRepository;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public PostService(PostRepository postRepository, MemberRepository memberRepository) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }
    public Long join(PostRequestDto postRequestDto){
        String memberId = postRequestDto.getMember();
        Member member = memberRepository.findByMemberId(memberId).get();

        Post post = postRequestDto.toEntity(member);
        postRepository.save(post);

        return post.getId();
    }
}
