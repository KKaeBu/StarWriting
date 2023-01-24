package star.starwriting.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member_tb")
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "member_id", length = 20, nullable = false)
    private String memberId;
    @Column(length = 20, nullable = false)
    private String password;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(nullable = false)
    private Date birthday;
    private String email;
    @Column(name = "phone_num")
    private String phoneNum;
    @Column(length = 10, nullable = false)
    private String nickname;
    private String tier;
    private String address;
    @Column(nullable = false)
    private String sex;
    @Column(name = "create_date")
    private String createDate;
//    이미지 파일


}
