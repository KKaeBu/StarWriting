package star.starwriting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import star.starwriting.domain.Member;
import star.starwriting.domain.Post;
import star.starwriting.dto.LikeRequestDto;
import star.starwriting.dto.MemberRequestDto;
import star.starwriting.dto.MemberResponseDto;
import star.starwriting.repository.MemberProfileImageRepository;
import star.starwriting.repository.MemberRepository;
import star.starwriting.repository.PostRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MemberService {


    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final MemberProfileImageRepository memberProfileImageRepository;
    private final ImageStore imageStore;
    private final JwtProvider jwtProvider;
    private final static int bcryptStrength = 10;

    @Autowired
    public MemberService(MemberRepository memberRepository,PostRepository postRepository, MemberProfileImageRepository memberProfileImageRepository,ImageStore imageStore, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.memberProfileImageRepository = memberProfileImageRepository;
        this.imageStore = imageStore;
        this.jwtProvider = jwtProvider;
        this.postRepository = postRepository;
    }

    /* 새로운 member 추가 함수*/
    public Long join(MemberRequestDto memberRequestDto, MultipartFile file) throws IOException {
        Member member = memberRequestDto.toEntity();
        validateDuplicateMember(member);

        // 현재 날짜 구하기
        LocalDateTime now = LocalDateTime.now();
        // 포맷 정의
        String formatDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        // 포맷 적용
        member.setCreateDate(formatDate);

        imageStore.storeImage(file,member); // 이미지를 로컬에 저장 후 DB 에도 이미지의 정보 저장
        memberRepository.save(member);
        return member.getId();
    }

    /* 로그인 함수 */
    public String Login(String inputMemberID, String inputPassword){
        Member member = memberRepository.findByMemberId(inputMemberID).get();
        System.out.println("입력한 유저: " + member.getMemberId());

        if(member.getMemberId() == null){
            System.out.println("잘못된 사용자 입력됨.");
            return null;
        }

        // Bcrypt 암호화
        String hashedPassword = member.getPassword();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(bcryptStrength);
        System.out.println("Bcrypt 비밀번호 대조 결과: "+bCryptPasswordEncoder.matches(inputPassword,hashedPassword));

        // 비밀번호가 맞다면
        if (bCryptPasswordEncoder.matches(inputPassword,hashedPassword)){
            String token = jwtProvider.createToken(member.getMemberId());
            boolean claims = jwtProvider.parseJwtToken("Bearer "+ token); // 토큰 검증
            return token;
        }else {
            System.out.println("잘못된 비밀번호 입력됨.");
            return null;
        }
    }

    public Member like(LikeRequestDto likeRequestDto) {
        String memberId = likeRequestDto.getMemberId();
        Long postId = likeRequestDto.getPostId();
        Member member = memberRepository.findByMemberId(memberId).get();
        Post post = postRepository.findById(postId).get();

        member.getLikePost().add(post);
        memberRepository.update(member);

        return member;
    }

    public List<Post> likeList(LikeRequestDto likeRequestDto){
        Member member = memberRepository.findByMemberId(likeRequestDto.getMemberId()).get();
        List<Post> likePost = member.getLikePost();

        System.out.println(likePost);
//        for(Post post:likePost){
//            System.out.println(post.getId());
//        }
        return likePost;
    }

    /* 모든 member 반환 */
    public List<MemberResponseDto> findAllMembers() {
        List<Member> memberList = memberRepository.findAll();
        List<MemberResponseDto> memberResponseDtoList = new ArrayList<>();

//        *****개선 필요할듯*****
        for(Member m : memberList) {
            memberResponseDtoList.
                    add(new MemberResponseDto(
                            m.getId(),
                            m.getMemberId(),
                            m.getName(),
                            m.getEmail(),
                            m.getNickname(),
                            m.getTier(),
                            m.getCreateDate(),
                            m.getProfileImage()
                        )
                    );
        }

        return memberResponseDtoList;
    }

    /* memberId로 유저 검색 (memberId 속성은 Unique이기에, 중복 x) */
    public Optional<MemberResponseDto> findMember(Long memberId) {
        Member member = memberRepository.findById(memberId).get();

        MemberResponseDto memberResponseDto = new MemberResponseDto(
                member.getId(),
                member.getMemberId(),
                member.getName(),
                member.getEmail(),
                member.getNickname(),
                member.getTier(),
                member.getCreateDate(),
                member.getProfileImage()
        );

        return Optional.ofNullable(memberResponseDto);
    }

    /* 중복 member 조회 */
    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
