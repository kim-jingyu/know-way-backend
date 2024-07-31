package com.knowway.departmentstore.entity;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
public class DepartmentStoreFloor {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long departmentStoreFloorId;
    @Column(nullable = false)
    private String departmentStoreFloorMapPath;
    @Column(nullable = false)
    private String departmentStoreFloor;

    @Setter
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "department_store_id")
    private DepartmentStore departmentStore;

    public static DepartmentStoreFloor createDepartmentStoreFloor(String departmentStoreFloor, String departmentStoreFloorMapPath) {
        return DepartmentStoreFloor.builder()
                .departmentStoreFloorMapPath(departmentStoreFloorMapPath)
                .departmentStoreFloor(departmentStoreFloor)
                .build();
    }
}
