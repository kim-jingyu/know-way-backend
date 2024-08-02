package com.knowway.user.service;

import com.knowway.auth.dto.UserChatMemberIdResponse;
import com.knowway.record.repository.RecordRepository;
import com.knowway.user.dto.MemberProfileDto;
import com.knowway.user.dto.UserProfileResponse;
import com.knowway.user.dto.UserRecordDto;
import com.knowway.user.dto.UserRecordResponse;
import com.knowway.user.dto.UserSignUpRequest;
import com.knowway.user.exception.UserException;
import com.knowway.user.mapper.UserMapper;
import com.knowway.user.repository.MemberRepository;
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
    memberRepository.save(
        UserMapper.INSTANCE.toMember(signUpDto, encoder.encode(signUpDto.getPassword())));
  }
    @Override
    public Page<UserRecordResponse> getUserRecordHistory(Long userId, int page, int size) {
      Pageable pageable = PageRequest.of(page, size);
      Page<UserRecordDto> dtoPage = recordRepository.findUserRecordsByMemberId(userId, pageable);
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


}
