package star.starwriting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import star.starwriting.domain.Member;
import star.starwriting.dto.MemberRequestDto;
import star.starwriting.dto.MemberResponseDto;
import star.starwriting.repository.MemberRepository;

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

        memberRepository.save(member);
        return member.getUserId();
    }

    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    public Optional<MemberResponseDto> findMember(Long memberId) {
        Member member = memberRepository.findById(memberId).get();

        MemberResponseDto memberResponseDto = new MemberResponseDto(
                member.getUserId(), member.getId(), member.getName(), member.getEmail(), member.getNickname(), member.getTier()
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
