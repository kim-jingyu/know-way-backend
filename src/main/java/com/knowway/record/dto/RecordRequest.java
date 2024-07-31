package com.knowway.record.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RecordRequest {
    private Long memberId;
    private Integer departmentStoreFloorId;
    private Long departmentStoreId;
    private String recordTitle;
    private String recordLatitude;
    private String recordLongitude;
}
