package com.knowway.user.service;

import com.knowway.auth.dto.UserChatMemberIdResponse;
import com.knowway.record.entity.Record;
import com.knowway.record.repository.RecordRepository;
import com.knowway.user.dto.MemberProfileDto;
import com.knowway.user.dto.UserProfileResponse;
import com.knowway.user.dto.UserRecordDto;
import com.knowway.user.dto.UserRecordResponse;
import com.knowway.user.dto.UserSignUpRequest;
import com.knowway.user.entity.Member;
import com.knowway.user.exception.UserException;
import com.knowway.user.mapper.UserMapper;
import com.knowway.user.repository.MemberRepository;
import com.knowway.user.vo.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final RecordRepository recordRepository;
  private final UserDuplicationChecker userDuplicationChecker;
  private final MemberRepository memberRepository;
  private final PasswordEncoder encoder;

  @Override
  public void signUp(UserSignUpRequest signUpDto) {
    userDuplicationChecker.emailDuplicationChecker(signUpDto.getEmail());
    Member member = UserMapper.INSTANCE.toMember(signUpDto,
        encoder.encode(signUpDto.getPassword()));
    memberRepository.save(member);
  }

  @Override
  public Page<UserRecordResponse> getUserRecordHistory(Long userId, Boolean isSelectedByAdmin,
      int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<UserRecordDto> dtoPage = recordRepository.findUserRecordsByMemberIdAndRecordIsSelected(
        userId, isSelectedByAdmin, pageable);
    return dtoPage.map(UserMapper.INSTANCE::userRecordDtoToResponse);
  }

  @Override
  public UserProfileResponse getUserProfileInfo(Long userid) {
    MemberProfileDto dto = memberRepository.findMemberEmailAndPointSum(userid);
    return UserMapper.INSTANCE.profileDtoToProfileResponse(dto);
  }

  @Override
  public UserChatMemberIdResponse getUserChatMemberId(Long userId) {
    Long chatId = memberRepository.getUserChatIdFromUserId(userId)
        .orElseThrow(() -> new UserException("존재하지 않은 유저입니다."));
    return UserChatMemberIdResponse.builder().memberChatId(chatId).build();
  }

  @Override
  public Role getRole(Long userId) {
    Member member = memberRepository.findById(userId)
        .orElseThrow(() -> new UserException("존재하지 않은 유저입니다."));
    return member.getRole();
  }


  public void deleteRecord(Long userId, Long recordId) {
    Record record = recordRepository.findByMemberIdAndId(userId, recordId)
        .orElseThrow(() -> new UserException("해당 유저에 속한 레코드가 아닙니다."));
    recordRepository.delete(record);
  }

}
