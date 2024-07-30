package com.knowway.departmentstore.repository;

import com.knowway.departmentstore.domain.DepartmentStore;
import com.knowway.departmentstore.exception.DepartmentStoreNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface  DepartmentStoreRepository extends JpaRepository<DepartmentStore, Long> {
    @Query("SELECT d FROM DepartmentStore d WHERE d.departmentStoreLatitude = :latitude AND d.departmentStoreLongtitude = :longtitude")
    Page<DepartmentStore> findPageByLatitudeLongtitude(String latitude, String longtitude, Pageable pageable);

    Optional<DepartmentStore> findByDepartmentStoreBranch(String departmentStoreBranch);

    default DepartmentStore getById(Long id) {
        return findById(id).orElseThrow(DepartmentStoreNotFoundException::new);
    }
}
