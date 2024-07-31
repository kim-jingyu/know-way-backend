package com.knowway.user.repository;

import com.knowway.user.entity.Member;
import com.knowway.user.vo.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByEmailAndPassword(String email, String password);

  Optional<Member> findById(Long id);

  Optional<Member> findByEmail(String email);

  @Query("SELECT m.role FROM Member m WHERE m.id = :id")
  Optional<Role> findRoleById(@Param("id") Long id);
}
