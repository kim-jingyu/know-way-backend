package com.knowway.admin.repository;

import com.knowway.record.entity.Record;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Record, Long> {
    @Query("SELECT Record FROM Record r WHERE r.departmentStoreFloor.departmentStoreFloorId =: departmentStoreFloorId ")
    List<Record> findByDepartmentStoreFloor(Long departmentStoreFloorId);
}
