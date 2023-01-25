package star.starwriting.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@DynamicInsert
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post_tb")
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id", nullable = false, referencedColumnName = "member_id")
    private Member member; // 외래키 설정해야함

    @Column(length = 20, nullable = false)
    private String title;

    @Column(name = "posting_date")
    @CreationTimestamp
    private Date postingDate; // default값으로 작성 시간 자동 추가 해야댐 *일단함

    @ColumnDefault("0")
    private Long view; // 얘도 조회수는 default값으로 0부터 해야댐

    @Column(name = "shared_num")
    @ColumnDefault("0")
    private Long sharedNum; // 글 공유 횟수
//    @Column(nullable = false)
//    private boolean approved; // 글 게시 승인 여부 근데 이런거 없애도 될듯..?? 프로젝트 규모 줄이기 일환으로
//    @Column(nullable = false, length = 20)
//    private String type; // 글 종류 (어떤 글 인지)
//    이미지 파일 추가해야됨
}
