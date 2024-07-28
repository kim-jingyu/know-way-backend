package com.knowway.admin.service;

import com.knowway.admin.dto.AdminRecordDto;
import com.knowway.admin.repository.AdminRepository;
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
    public List<AdminRecordDto> getRecordsByFloorId(Integer floorId) {
        return adminRepository.findByDepartmentStoreFloorId(floorId).stream()
                .map(record -> AdminRecordDto.builder()
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
        return adminRepository.findById(id).map(record -> {
            record.updateRecordIsSelected(!record.getRecordIsSelected());
            adminRepository.save(record);
            return true;
        }).orElse(false);
    }
}
