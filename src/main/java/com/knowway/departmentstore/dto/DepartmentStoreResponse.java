package com.knowway.departmentstore.dto;

import com.knowway.departmentstore.domain.DepartmentStore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DepartmentStoreResponse {
    private Long departmentStoreId;
    private String departmentStoreName;
    private String departmentStoreBranch;

    public static DepartmentStoreResponse of(DepartmentStore departmentStore) {
        return DepartmentStoreResponse.builder()
                .departmentStoreId(departmentStore.getDepartmentStoreId())
                .departmentStoreName(departmentStore.getDepartmentStoreName())
                .departmentStoreBranch(departmentStore.getDepartmentStoreBranch())
                .build();
    }
}
