package com.knowway.departmentstore.entity;

import com.knowway.record.entity.Record;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

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

    @Builder.Default
    @OneToMany(mappedBy = "departmentStoreFloor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Record> recordList = new ArrayList<>();

    public static DepartmentStoreFloor createDepartmentStoreFloor(String departmentStoreFloor, String departmentStoreFloorMapPath) {
        return DepartmentStoreFloor.builder()
                .departmentStoreFloorMapPath(departmentStoreFloorMapPath)
                .departmentStoreFloor(departmentStoreFloor)
                .build();
    }
}
