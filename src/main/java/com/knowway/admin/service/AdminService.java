package com.knowway.admin.service;

import com.knowway.admin.dto.AdminRecordDto;

import java.util.List;

public interface AdminService {
    List<AdminRecordDto> getRecordsByFloorId(Integer floorId);
    boolean toggleRecordIsSelected(Long id);
}