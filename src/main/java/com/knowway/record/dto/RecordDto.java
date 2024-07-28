package com.knowway.record.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RecordDto {
    private Long memberId;
    private Integer departmentStoreFloorId;
    private Long departmentStoreId;
    private String recordTitle;
    private String recordLatitude;
    private String recordLongitude;
}
