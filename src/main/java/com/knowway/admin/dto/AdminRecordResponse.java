package com.knowway.admin.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class AdminRecordResponse {
    private Long id;
    private String recordTitle;
    private String recordLatitude;
    private String recordLongitude;
    private String recordPath;
    private Long recordArea;
    private Boolean recordIsSelected;
    private Long memberId;
}
