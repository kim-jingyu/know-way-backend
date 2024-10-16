package com.knowway.record.entity;


import com.knowway.common.entity.BaseEntity;
import com.knowway.departmentstore.entity.DepartmentStore;
import com.knowway.departmentstore.entity.DepartmentStoreFloor;
import com.knowway.user.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(name = "record")
public class Record extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long id;

    @JoinColumn(name = "department_store_floor_id")
    @ManyToOne
    private DepartmentStoreFloor departmentStoreFloor;

    @JoinColumn(name = "department_store_id")
    @ManyToOne
    private DepartmentStore departmentStore;

    @Column(name = "record_title", nullable = false)
    private String recordTitle;

    @Column(name = "record_latitude", nullable = false)
    private String recordLatitude;

    @Column(name = "record_longitude", nullable = false)
    private String recordLongitude;

    @Column(name = "record_path", nullable = false)
    private String recordPath;

    @Column(name = "record_is_selected", nullable = false)
    private Boolean recordIsSelected = false;

    @Column(name = "record_area", nullable = false)
    private Long recordArea;

    @JoinColumn(name = "member_id")
    @ManyToOne
    private Member member;

}
