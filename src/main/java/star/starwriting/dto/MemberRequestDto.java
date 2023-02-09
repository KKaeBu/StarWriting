package star.starwriting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import star.starwriting.domain.Member;
import star.starwriting.service.MemberService;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {

    private String memberId;
    private String password;
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private String email;
    private String phoneNum;
    private String nickname;
    private String tier;
    private String address;
    private String sex;

    public Member toEntity() {
        return Member.builder()
                .memberId(this.memberId)
                .password(new BCryptPasswordEncoder(10).encode(this.password)) /* password Bcrypt로 암호화 후 DB에 저장 */
                .name(this.name)
                .birthday(this.birthday)
                .email(this.email)
                .phoneNum(this.phoneNum)
                .nickname(this.nickname)
                .tier(this.tier)
                .address(this.address)
                .sex(this.sex)
                .build();
    }
}
