package com.knowway.departmentstore.domain;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotNull;

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

    @ManyToOne(fetch = LAZY)
    @Setter
    private DepartmentStore departmentStore;

    public static DepartmentStoreFloor createDepartmentStoreFloor(String departmentStoreFloor, String departmentStoreFloorMapPath) {
        return DepartmentStoreFloor.builder()
                .departmentStoreFloorMapPath(departmentStoreFloorMapPath)
                .departmentStoreFloor(departmentStoreFloor)
                .build();
    }
}
