package com.knowway.departmentstore.repository;

import com.knowway.departmentstore.domain.DepartmentStoreFloor;
import com.knowway.departmentstore.exception.DepartmentStoreFloorNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentStoreFloorRepository extends JpaRepository<DepartmentStoreFloor, Long> {
    default DepartmentStoreFloor getById(Long id) {
        return findById(id).orElseThrow(DepartmentStoreFloorNotFoundException::new);
    }
}
