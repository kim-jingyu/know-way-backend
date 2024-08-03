package com.knowway.user.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UserRecordDto {

  private Long recordId;
  private String recordUrl;
  private String recordTitle;
  private Boolean isSelectedByAdmin;
  private String departmentName;
  private String departmentLocationName;
  private String floor;
}