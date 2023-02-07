package star.starwriting.service;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import star.starwriting.domain.Member;
import star.starwriting.dto.MemberRequestDto;
import star.starwriting.dto.MemberResponseDto;
import star.starwriting.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MemberService {


    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final static int bcryptStrength = 10;
    @Autowired
    public MemberService(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    public Long join(MemberRequestDto memberRequestDto) {
        Member member = memberRequestDto.toEntity();
        validateDuplicateMember(member);

        memberRepository.save(member);
        return member.getId();
    }

    public String Login(String inputMemberID, String inputPassword){
        Member member = memberRepository.findByMemberId(inputMemberID).get();
        System.out.println("입력한 유저: " + member.getMemberId());

        String hashedPassword = member.getPassword();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(bcryptStrength);
        System.out.println("Bcrypt 비밀번호 대조 결과: "+bCryptPasswordEncoder.matches(inputPassword,hashedPassword));

        // 비밀번호가 맞다면
        if (bCryptPasswordEncoder.matches(inputPassword,hashedPassword)){
            String token = jwtProvider.createToken(member.getMemberId());
            boolean claims = jwtProvider.parseJwtToken("Bearer "+ token); // 토큰 검증

            return token;
        }else {
            return null;
        }
    }

    public List<MemberResponseDto> findAllMembers() {
        List<Member> memberList = memberRepository.findAll();
        List<MemberResponseDto> memberResponseDtoList = new ArrayList<>();

//        *****개선 필요할듯*****
        for(Member m : memberList) {
            memberResponseDtoList.
                    add(new MemberResponseDto(
                            m.getId(), m.getMemberId(), m.getName(), m.getEmail(), m.getNickname(), m.getTier()
                        )
                    );
        }

        return memberResponseDtoList;
    }

    public Optional<MemberResponseDto> findMember(Long memberId) {
        Member member = memberRepository.findById(memberId).get();

        MemberResponseDto memberResponseDto = new MemberResponseDto(
                member.getId(), member.getMemberId(), member.getName(), member.getEmail(), member.getNickname(), member.getTier()
        );

        return Optional.ofNullable(memberResponseDto);
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }
}
