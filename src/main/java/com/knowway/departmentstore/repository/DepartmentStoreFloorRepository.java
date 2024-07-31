package com.knowway.departmentstore.repository;

import com.knowway.departmentstore.entity.DepartmentStore;
import com.knowway.departmentstore.entity.DepartmentStoreFloor;
import com.knowway.departmentstore.exception.DepartmentStoreFloorNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentStoreFloorRepository extends JpaRepository<DepartmentStoreFloor, Long> {
    DepartmentStoreFloor findByDepartmentStoreAndDepartmentStoreFloor(DepartmentStore departmentStore, String departmentStoreFloor);
    default DepartmentStoreFloor getById(Long id) {
        return findById(id).orElseThrow(DepartmentStoreFloorNotFoundException::new);
    }
}
