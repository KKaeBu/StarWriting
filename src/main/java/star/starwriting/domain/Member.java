package star.starwriting.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member_tb")
@Entity
public class Member implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "member_id", length = 20, nullable = false, unique = true)
    private String memberId;
    @Column(length = 100, nullable = false)
    private String password;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private String email;
    @Column(name = "phone_num")
    private String phoneNum;
    @Column(length = 15)
    private String nickname;
    private String tier;
    private String address;
    @Column(nullable = false)
    private String sex;
//    이미지 파일


}
