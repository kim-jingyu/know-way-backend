package com.knowway.admin.repository;

import com.knowway.record.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Record, Long> {
    List<Record> findByDepartmentStoreFloorId(Integer departmentStoreFloorId);
}
