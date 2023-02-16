package star.starwriting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import star.starwriting.domain.Like;
import star.starwriting.domain.Member;
import star.starwriting.domain.Post;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeRequestDto {
    private String member;
    private Long post;

    public Like toEntity(Member member, Post post) {
        return Like.builder()
                .member(member)
                .post(post)
                .build();
    }
}
