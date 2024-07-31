package com.knowway.user.mapper;

import com.knowway.user.dto.MemberProfileDto;
import com.knowway.user.dto.UserRecordDto;
import com.knowway.user.dto.UserRecordResponse;
import com.knowway.user.dto.UserSignUpRequest;
import com.knowway.user.entity.Member;
import com.knowway.user.dto.UserProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "passwordEncoder")
    @Mapping(target = "role", constant= "USER")
    Member toMember(UserSignUpRequest dto, String passwordEncoder);


    UserSignUpRequest toDto(Member member);


    UserProfileResponse profileDtoToProfileResponse(MemberProfileDto memberProfileDto);


    @Mapping(source = "recordId", target = "recordId")
    @Mapping(source = "recordUrl", target = "recordUrl")
    @Mapping(source = "isSelectedByAdmin", target = "isSelectedByAdmin")
    @Mapping(source = "departmentName", target = "departmentName")
    @Mapping(source = "departmentLocationName", target = "departmentLocationName")
    @Mapping(source = "floor", target = "floor")
    UserRecordResponse userRecordDtoToResponse(UserRecordDto userRecordDto);
}
