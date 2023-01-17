package star.starwriting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private Long userId;
    private String id;
    private String name;
    private String email;
    private String nickname;
    private String tier;
}
