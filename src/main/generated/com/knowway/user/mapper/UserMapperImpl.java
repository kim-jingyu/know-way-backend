package com.knowway.user.mapper;

import com.knowway.user.dto.UserSignUpDto;
import com.knowway.user.entity.Member;
import com.knowway.user.vo.Role;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-28T19:59:05+0900",
    comments = "version: 1.5.0.Final, compiler: javac, environment: Java 17.0.11 (Amazon.com Inc.)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public Member toMember(UserSignUpDto dto, String passwordEncoder) {
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
    public UserSignUpDto toDto(Member member) {
        if ( member == null ) {
            return null;
        }

        UserSignUpDto.UserSignUpDtoBuilder userSignUpDto = UserSignUpDto.builder();

        userSignUpDto.email( member.getEmail() );
        userSignUpDto.password( member.getPassword() );

        return userSignUpDto.build();
    }
}
