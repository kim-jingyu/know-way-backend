package com.knowway.admin.service;

import com.knowway.admin.dto.AdminRecordResponse;
import com.knowway.admin.repository.AdminRepository;
import com.knowway.admin.exception.AdminException;
import com.knowway.record.entity.Record;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;

    @Override
    public List<AdminRecordResponse> getRecordsByFloorId(Integer floorId) {
        return adminRepository.findByDepartmentStoreFloorId(floorId).stream()
                .map(record -> AdminRecordResponse.builder()
                        .id(record.getId())
                        .recordTitle(record.getRecordTitle())
                        .recordLatitude(record.getRecordLatitude())
                        .recordLongitude(record.getRecordLongitude())
                        .recordPath(record.getRecordPath())
                        .recordIsSelected(record.getRecordIsSelected())
                        .memberId(record.getMember().getId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean toggleRecordIsSelected(Long id) {
        Record record = adminRepository.findById(id)
                .orElseThrow(() -> new AdminException("id 값이 올바르지 않습니다."));

        Record updatedRecord = Record.builder()
                .id(record.getId())
                .member(record.getMember())
                .departmentStoreFloorId(record.getDepartmentStoreFloorId())
                .departmentStoreId(record.getDepartmentStoreId())
                .recordTitle(record.getRecordTitle())
                .recordLatitude(record.getRecordLatitude())
                .recordLongitude(record.getRecordLongitude())
                .recordPath(record.getRecordPath())
                .recordIsSelected(!record.getRecordIsSelected())
                .build();

        adminRepository.save(updatedRecord);

        return true;
    }
}
