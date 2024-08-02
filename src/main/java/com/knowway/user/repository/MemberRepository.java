package com.knowway.user.repository;

import com.knowway.user.dto.MemberProfileDto;
import com.knowway.user.entity.Member;
import java.util.Optional;
import java.util.OptionalLong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

  @Query("SELECT new com.knowway.user.dto.MemberProfileDto(m.email, COUNT(p.id)) " +
      "FROM Member m LEFT JOIN m.pointList p " +
      "wHERE m.id =:id " +
      "GROUP BY m.email")
  MemberProfileDto findMemberEmailAndPointSum(Long id);

  Optional<Member> findById(Long id);

  Optional<Member> findByChatId(Long id);

  Optional<Member> findByEmail(String email);

  @Query("SELECT m.chatId " +
      "FROM Member m "+
      "wHERE m.id =:userId ")
  Optional<Long> getUserChatIdFromUserId(Long userId);


}
