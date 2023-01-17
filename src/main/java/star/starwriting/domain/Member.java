package star.starwriting.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_tb")
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    @Column(length = 20, nullable = false)
    private String id;
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
//    이미지 파일


}
