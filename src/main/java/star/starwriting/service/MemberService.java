package star.starwriting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import star.starwriting.domain.Member;
import star.starwriting.domain.MemberProfileImage;
import star.starwriting.dto.MemberProfileImageDto;
import star.starwriting.dto.MemberRequestDto;
import star.starwriting.dto.MemberResponseDto;
import star.starwriting.repository.MemberProfileImageRepository;
import star.starwriting.repository.MemberRepository;

import java.io.File;
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

    @Autowired
    public MemberService(MemberRepository memberRepository, MemberProfileImageRepository memberProfileImageRepository) {
        this.memberRepository = memberRepository;
        this.memberProfileImageRepository = memberProfileImageRepository;
    }

    public Long join(MemberRequestDto memberRequestDto) {
        Member member = memberRequestDto.toEntity();
        validateDuplicateMember(member);

        // 현재 날짜 구하기
        LocalDateTime now = LocalDateTime.now();
        // 포맷 정의
        String formatDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        // 포맷 적용
        member.setCreateDate(formatDate);

        /*
        * 맴버 프로필 기본 이미지 저장하기
        * */
//        1. 프로젝트 폴더내의 기본 이미지 가져오기

        String fileUrl = "src/main/resources/static/img/basicProfile.png";
        File basicImgFile = new File(fileUrl);

//        2. db에 BLOB 방식으로 저장
        String originalFileName = basicImgFile.getName();
        String storedImgFileName = "basicProfile" + "." + extractExt(basicImgFile.getName());

        MemberProfileImageDto profileImageDto = new MemberProfileImageDto(
                originalFileName,
                storedImgFileName,
                fileUrl
        );

        MemberProfileImage profileImage = profileImageDto.toEntity();

        member.setProfileImage(profileImage);

        memberRepository.save(member);
        memberProfileImageRepository.save(profileImage);
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
