package star.starwriting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import star.starwriting.domain.MemberProfileImage;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String memberId;
    private String name;
    private String email;
    private String nickname;
    private String tier;
    private String createDate;
    private MemberProfileImage profileImage;
}
