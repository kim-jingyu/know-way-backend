package com.knowway.departmentstore.dto;

import com.knowway.departmentstore.entity.DepartmentStore;
import com.knowway.departmentstore.entity.DepartmentStoreFloor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Data
@Builder
@AllArgsConstructor
public class DepartmentStoreResponse {
    private Long departmentStoreId;
    private String departmentStoreName;
    private String departmentStoreBranch;
    @Builder.Default
    private List<DepartmentStoreFloorResponse> departmentStoreFloorResponseList = new ArrayList<>();

    public static DepartmentStoreResponse of(DepartmentStore departmentStore) {
        DepartmentStoreResponse response = DepartmentStoreResponse.builder()
                .departmentStoreId(departmentStore.getDepartmentStoreId())
                .departmentStoreName(departmentStore.getDepartmentStoreName())
                .departmentStoreBranch(departmentStore.getDepartmentStoreBranch())
                .build();
        for (DepartmentStoreFloor departmentStoreFloor : departmentStore.getDepartmentStoreFloorList()) {
            response.addDepartmentFloorResponse(DepartmentStoreFloorResponse.of(departmentStoreFloor));
        }
        return response;
    }

    private void addDepartmentFloorResponse(DepartmentStoreFloorResponse departmentStoreFloorResponse) {
        departmentStoreFloorResponseList.add(departmentStoreFloorResponse);
    }
}
