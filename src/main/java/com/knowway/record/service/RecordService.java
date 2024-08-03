package com.knowway.record.service;

import com.knowway.departmentstore.dto.DepartmentStoreFloorMapResponse;
import com.knowway.record.dto.RecordRequest;
import com.knowway.record.dto.RecordResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecordService {
    Long addRecord(RecordRequest recordRequest, MultipartFile recordFile);
    List<RecordResponse> findSelectedRecord(Long departmentStoreId, Long departmentStoreFloorId);
}
