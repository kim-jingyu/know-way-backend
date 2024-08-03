package com.knowway.record.repository;

import com.knowway.record.dto.RecordResponse;
import com.knowway.record.entity.Record;
import com.knowway.user.dto.UserRecordDto;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {


  @Query("SELECT new com.knowway.user.dto.UserRecordDto(" +
      "r.id, r.recordPath, r.recordTitle,r.recordIsSelected," +
      "ds.departmentStoreName,ds.departmentStoreBranch,dsf.departmentStoreFloor) " +
      "FROM Record r " +
      "JOIN r.departmentStoreFloor dsf " +
      "JOIN dsf.departmentStore ds " +
      "WHERE r.member.id = :memberId")
  Page<UserRecordDto> findUserRecordsByMemberId(@Param("memberId") Long memberId,
      Pageable pageable);

  Optional<Record> findByMemberIdAndId(Long memberId, Long recordId);

  @Query("SELECT new com.knowway.record.dto.RecordResponse(" +
          "r.id, r.recordTitle, r.recordLatitude, r.recordLongitude, r.recordPath) " +
          "FROM Record r " +
          "JOIN r.departmentStoreFloor dsf " +
          "JOIN r.departmentStore ds " +
          "WHERE ds.departmentStoreId = :departmentStoreId " +
          "AND dsf.departmentStoreFloorId = :departmentStoreFloorId " +
          "AND r.recordIsSelected = true")
  List<RecordResponse> findSelectedRecordsByDepartmentStoreIdAndFloor(
          @Param("departmentStoreId") Long departmentStoreId,
          @Param("departmentStoreFloorId") Long departmentStoreFloorId);


}