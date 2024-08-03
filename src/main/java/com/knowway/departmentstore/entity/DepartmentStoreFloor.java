package com.knowway.departmentstore.entity;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "department_store_floor",
    uniqueConstraints = @UniqueConstraint(columnNames = {"department_store_id",
        "department_store_floor"}))
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
