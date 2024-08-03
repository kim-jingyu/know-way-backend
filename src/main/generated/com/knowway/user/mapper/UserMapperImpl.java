package com.knowway.user.mapper;

import com.knowway.user.dto.MemberProfileDto;
import com.knowway.user.dto.UserProfileResponse;
import com.knowway.user.dto.UserRecordDto;
import com.knowway.user.dto.UserRecordResponse;
import com.knowway.user.dto.UserSignUpRequest;
import com.knowway.user.entity.Member;
import com.knowway.user.vo.Role;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-03T21:15:15+0900",
    comments = "version: 1.5.0.Final, compiler: javac, environment: Java 17.0.11 (Amazon.com Inc.)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public Member toMember(UserSignUpRequest dto, String passwordEncoder) {
        if ( dto == null && passwordEncoder == null ) {
            return null;
        }

        Member.MemberBuilder member = Member.builder();

        if ( dto != null ) {
            member.email( dto.getEmail() );
        }
        member.password( passwordEncoder );
        member.role( Role.ROLE_USER );

        return member.build();
    }

    @Override
    public UserSignUpRequest toDto(Member member) {
        if ( member == null ) {
            return null;
        }

        UserSignUpRequest.UserSignUpRequestBuilder userSignUpRequest = UserSignUpRequest.builder();

        userSignUpRequest.email( member.getEmail() );
        userSignUpRequest.password( member.getPassword() );

        return userSignUpRequest.build();
    }

    @Override
    public UserProfileResponse profileDtoToProfileResponse(MemberProfileDto memberProfileDto) {
        if ( memberProfileDto == null ) {
            return null;
        }

        UserProfileResponse.UserProfileResponseBuilder userProfileResponse = UserProfileResponse.builder();

        userProfileResponse.email( memberProfileDto.getEmail() );
        userProfileResponse.pointTotal( memberProfileDto.getPointTotal() );

        return userProfileResponse.build();
    }

    @Override
    public UserRecordResponse userRecordDtoToResponse(UserRecordDto userRecordDto) {
        if ( userRecordDto == null ) {
            return null;
        }

        UserRecordResponse.UserRecordResponseBuilder userRecordResponse = UserRecordResponse.builder();

        userRecordResponse.recordId( userRecordDto.getRecordId() );
        userRecordResponse.recordUrl( userRecordDto.getRecordUrl() );
        userRecordResponse.isSelectedByAdmin( userRecordDto.getIsSelectedByAdmin() );
        userRecordResponse.departmentName( userRecordDto.getDepartmentName() );
        userRecordResponse.departmentLocationName( userRecordDto.getDepartmentLocationName() );
        if ( userRecordDto.getFloor() != null ) {
            userRecordResponse.floor( Integer.parseInt( userRecordDto.getFloor() ) );
        }

        return userRecordResponse.build();
    }
}
