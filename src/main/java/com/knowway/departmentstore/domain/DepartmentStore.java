package com.knowway.departmentstore.domain;

import com.knowway.chat.entity.ChatMessage;
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
public class DepartmentStore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentStoreId;
    @Column(nullable = false)
    private String departmentStoreName;
    @Column(nullable = false)
    private String departmentStoreBranch;
    @Column(nullable = false)
    private String departmentStoreLatitude;
    @Column(nullable = false)
    private String departmentStoreLongtitude;

    @Builder.Default
    @OneToMany(mappedBy = "departmentStore", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<DepartmentStoreFloor> departmentStoreFloorList = new ArrayList<>();

    @OneToMany(mappedBy = "departmentStore")
    private List<ChatMessage> chatMessageList;

    public static DepartmentStore createDepartmentStore(DepartmentStoreRequest request, List<DepartmentStoreFloor> departmentStoreFloorList) {
        DepartmentStore departmentStore = DepartmentStore.builder()
                .departmentStoreName(request.getDepartmentStoreName())
                .departmentStoreBranch(request.getDepartmentStoreBranch())
                .departmentStoreLatitude(request.getDepartmentStoreLatitude())
                .departmentStoreLongtitude(request.getDepartmentStoreLongtitude())
                .build();
        for (DepartmentStoreFloor departmentStoreFloor : departmentStoreFloorList) {
            departmentStoreFloor.setDepartmentStore(departmentStore);
            List<DepartmentStoreFloor> departmentStoreFloorList1 = departmentStore.getDepartmentStoreFloorList();
            departmentStoreFloorList1.add(departmentStoreFloor);
        }
        return departmentStore;
    }
}
