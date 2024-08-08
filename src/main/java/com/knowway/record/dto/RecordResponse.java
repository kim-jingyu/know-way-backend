package com.knowway.record.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class RecordResponse {
    private Long recordId;
    private String recordTitle;
    private String recordLatitude;
    private String recordLongitude;
    private String recordPath;
    private Long floorId;
}
