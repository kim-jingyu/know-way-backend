package com.knowway.user.mapper;

import com.knowway.user.dto.UserSignUpRequest;
import com.knowway.user.entity.Member;
import com.knowway.user.vo.Role;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-31T16:04:20+0900",
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
        member.role( Role.USER );

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
}
