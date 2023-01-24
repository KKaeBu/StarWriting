package star.starwriting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import star.starwriting.domain.Member;
import star.starwriting.dto.MemberRequestDto;
import star.starwriting.dto.MemberResponseDto;
import star.starwriting.repository.MemberRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long join(MemberRequestDto memberRequestDto) {
        Member member = memberRequestDto.toEntity();
        validateDuplicateMember(member);

        // 현재 날짜 구하기
        LocalDate now = LocalDate.now();
        // 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        // 포맷 적용
        String formatedNow = now.format(formatter);

        member.setCreateDate(formatedNow);

        memberRepository.save(member);
        return member.getId();
    }

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
                            m.getCreateDate()
                        )
                    );
        }

        return memberResponseDtoList;
    }

    public Optional<MemberResponseDto> findMember(Long memberId) {
        Member member = memberRepository.findById(memberId).get();

        MemberResponseDto memberResponseDto = new MemberResponseDto(
                member.getId(),
                member.getMemberId(),
                member.getName(),
                member.getEmail(),
                member.getNickname(),
                member.getTier(),
                member.getCreateDate()
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
