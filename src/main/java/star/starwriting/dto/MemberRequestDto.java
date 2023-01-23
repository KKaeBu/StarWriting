package star.starwriting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import star.starwriting.domain.Member;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.text.SimpleDateFormat;
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
                .password(this.password)
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
