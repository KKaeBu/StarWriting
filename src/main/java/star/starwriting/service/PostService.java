package star.starwriting.service;

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
