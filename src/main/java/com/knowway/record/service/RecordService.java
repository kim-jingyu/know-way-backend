package com.knowway.record.service;

import com.knowway.record.dto.RecordRequest;
import org.springframework.web.multipart.MultipartFile;

public interface RecordService {
    Long addRecord(RecordRequest recordRequest, MultipartFile recordFile);
}
