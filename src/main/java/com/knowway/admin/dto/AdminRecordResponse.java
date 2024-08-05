package com.knowway.admin.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class AdminRecordResponse {
    private Long recordId;
    private String recordTitle;
    private String recordPath;
}
