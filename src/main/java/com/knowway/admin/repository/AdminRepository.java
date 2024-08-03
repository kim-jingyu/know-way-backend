package com.knowway.admin.repository;

import com.knowway.record.entity.Record;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Record, Long> {
    @Query("SELECT r FROM Record r WHERE r.departmentStoreFloor.departmentStoreFloorId = :departmentStoreFloorId AND r.recordArea = :recordArea AND r.recordIsSelected = :recordIsSelected")
    List<Record> findByDepartmentStoreFloor(Long departmentStoreFloorId, Long recordArea, Boolean recordIsSelected);
}
