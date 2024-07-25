package com.knowway.user.mapper;

import com.knowway.user.dto.UserSignUpDto;
import com.knowway.user.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "passwordEncoder")
    @Mapping(target = "role", constant= "ROLE_USER")
    Member toMember(UserSignUpDto dto,  String passwordEncoder);


    UserSignUpDto toDto(Member member);



}
