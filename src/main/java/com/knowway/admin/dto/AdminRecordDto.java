package com.knowway.admin.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AdminRecordDto {
    private Long id;
    private String recordTitle;
    private String recordLatitude;
    private String recordLongitude;
    private String recordPath;
    private Boolean recordIsSelected;
    private Long memberId;
}
