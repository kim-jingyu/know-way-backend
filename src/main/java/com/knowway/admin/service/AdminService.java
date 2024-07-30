package com.knowway.admin.service;

import com.knowway.admin.dto.AdminRecordResponse;

import java.util.List;

public interface AdminService {
    List<AdminRecordResponse> getRecordsByFloorId(Integer floorId);
    boolean toggleRecordIsSelected(Long id);
}