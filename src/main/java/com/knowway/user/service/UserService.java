package com.knowway.user.service;

import com.knowway.auth.dto.UserChatMemberIdResponse;
import com.knowway.user.dto.UserProfileResponse;
import com.knowway.user.dto.UserRecordResponse;
import com.knowway.user.dto.UserSignUpRequest;
import com.knowway.user.vo.Role;
import org.springframework.data.domain.Page;

public interface UserService {

  void signUp(UserSignUpRequest signUpDto);

  void deleteRecord(Long userId, Long recordId);

  Page<UserRecordResponse> getUserRecordHistory(Long userid, Boolean isSelectedByAdmin,int page, int size);

  UserProfileResponse getUserProfileInfo(Long userid);
  UserChatMemberIdResponse getUserChatMemberId(Long userId);
  Role getRole(Long userId);
}
