package com.knowway.departmentstore.repository;

import com.knowway.departmentstore.entity.DepartmentStore;
import com.knowway.departmentstore.exception.DepartmentStoreNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface  DepartmentStoreRepository extends JpaRepository<DepartmentStore, Long> {
    @Query("SELECT d FROM DepartmentStore d WHERE d.departmentStoreLatitude = :latitude AND d.departmentStoreLongitude = :longitude")
    Page<DepartmentStore> findPageByLatitudeLongitude(String latitude, String longitude, Pageable pageable);

    @Query("SELECT d FROM DepartmentStore d WHERE LOWER(d.departmentStoreBranch) LIKE LOWER(CONCAT('%', :departmentStoreBranch ,'%'))")
    List<DepartmentStore> findByDepartmentStoreBranchContainingIgnoreCase(String departmentStoreBranch);

    default DepartmentStore getById(Long id) {
        return findById(id).orElseThrow(DepartmentStoreNotFoundException::new);
    }
}
