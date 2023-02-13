package star.starwriting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import star.starwriting.domain.Member;
import star.starwriting.domain.MemberProfileImage;
import star.starwriting.dto.MemberProfileImageDto;
import star.starwriting.dto.MemberRequestDto;
import star.starwriting.dto.MemberResponseDto;
import star.starwriting.repository.MemberProfileImageRepository;
import star.starwriting.repository.MemberRepository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberProfileImageRepository memberProfileImageRepository;
    private final ImageStore imageStore;

    @Autowired
    public MemberService(MemberRepository memberRepository, MemberProfileImageRepository memberProfileImageRepository,ImageStore imageStore) {
        this.memberRepository = memberRepository;
        this.memberProfileImageRepository = memberProfileImageRepository;
        this.imageStore = imageStore;
    }

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

    public Optional<MemberResponseDto> findMember(Long memberId) {
        Member member = memberRepository.findById(memberId).get();

        System.out.println(member.getProfileImage().getFileUrl());

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
