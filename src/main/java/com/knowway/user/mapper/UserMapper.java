package com.knowway.user.mapper;

import com.knowway.user.dto.UserSignUpRequest;
import com.knowway.user.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "passwordEncoder")
    @Mapping(target = "role", constant= "ROLE_USER")
    Member toMember(UserSignUpRequest dto,  String passwordEncoder);


    UserSignUpRequest toDto(Member member);



}
