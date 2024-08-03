package com.knowway.departmentstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.knowway.chat.entity.ChatMessage;
import com.knowway.common.entity.BaseEntity;
import com.knowway.departmentstore.dto.DepartmentStoreRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
public class DepartmentStore extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentStoreId;
    @Column(nullable = false)
    private String departmentStoreName;
    @Column(nullable = false,unique = true)
    private String departmentStoreBranch;
    @Column(nullable = false)
    private Double departmentStoreLatitude;
    @Column(nullable = false)
    private Double departmentStoreLongitude;

    @Builder.Default
    @OneToMany(mappedBy = "departmentStore", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DepartmentStoreFloor> departmentStoreFloorList = new ArrayList<>();

    @OneToMany(mappedBy = "departmentStore")
    @JsonIgnore
    private List<ChatMessage> chatMessageList;

    public void addDeptFloor(DepartmentStoreFloor departmentStoreFloor) {
        departmentStoreFloorList.add(departmentStoreFloor);
        departmentStoreFloor.setDepartmentStore(this);
    }

    public static DepartmentStore createDepartmentStore(DepartmentStoreRequest request, List<DepartmentStoreFloor> departmentStoreFloorList) {
        DepartmentStore departmentStore = DepartmentStore.builder()
                .departmentStoreName(request.getDepartmentStoreName())
                .departmentStoreBranch(request.getDepartmentStoreBranch())
                .departmentStoreLatitude(request.getDepartmentStoreLatitude())
                .departmentStoreLongitude(request.getDepartmentStoreLongitude())
                .build();
        for (DepartmentStoreFloor departmentStoreFloor : departmentStoreFloorList) {
            departmentStore.addDeptFloor(departmentStoreFloor);
        }
        return departmentStore;
    }
}
