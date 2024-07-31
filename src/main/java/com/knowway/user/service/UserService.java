package com.knowway.user.service;

<<<<<<< Updated upstream
=======
import com.knowway.user.dto.UserProfileResponse;
import com.knowway.user.dto.UserRecordResponse;
>>>>>>> Stashed changes
import com.knowway.user.dto.UserSignUpRequest;
import org.springframework.data.domain.Page;

public interface UserService {
  void signUp(UserSignUpRequest signUpDto);
<<<<<<< Updated upstream
=======
  Page<UserRecordResponse> getUserRecordHistory(Long userid, int page, int size);
  UserProfileResponse getUserProfileInfo(Long userid);
>>>>>>> Stashed changes
}
