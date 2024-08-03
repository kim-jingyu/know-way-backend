package com.knowway.record.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class RecordResponse {
    private Long recordId;
    private String recordLatitude;
    private String recordLongitude;
    private String recordPath;
}
