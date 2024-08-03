package com.knowway.record.service;

import com.knowway.chat.dto.ChatMessageResponse;
import com.knowway.departmentstore.dto.DepartmentStoreFloorMapResponse;
import com.knowway.departmentstore.entity.DepartmentStore;
import com.knowway.departmentstore.entity.DepartmentStoreFloor;
import com.knowway.departmentstore.repository.DepartmentStoreFloorRepository;
import com.knowway.departmentstore.repository.DepartmentStoreRepository;
import com.knowway.record.dto.RecordRequest;
import com.knowway.record.dto.RecordResponse;
import com.knowway.record.entity.Record;
import com.knowway.record.repository.RecordRepository;
import com.knowway.s3.exception.S3Exception;
import com.knowway.s3.service.S3UploadService;
import com.knowway.user.entity.Member;
import com.knowway.user.exception.UserException;
import com.knowway.user.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class RecordServiceImpl implements RecordService {

    private final DepartmentStoreFloorRepository storeFloorRepository;
    private final DepartmentStoreRepository departmentStoreRepository;
    private final RecordRepository recordRepository;
    private final MemberRepository memberRepository;
    private final S3UploadService s3UploadService;

    @Transactional
    public Long addRecord(RecordRequest recordRequest, MultipartFile recordFile) {
        String recordUrl = null;
        try {
            recordUrl = s3UploadService.saveFile(recordFile, "record");
        } catch (IOException e) {
            throw new S3Exception();
        }
        DepartmentStoreFloor departmentStoreFloor = storeFloorRepository.getById(
            recordRequest.getDepartmentStoreFloorId());

        DepartmentStore departmentStore = departmentStoreRepository.getById(
                recordRequest.getDepartmentStoreId());
        Member member = memberRepository.findById(recordRequest.getMemberId())
            .orElseThrow(() -> new UserException("유효하지 않은 아이디입니다."));

        Record record = Record.builder()
            .member(member)
            .departmentStore(departmentStore)
            .departmentStoreFloor(departmentStoreFloor)
            .recordTitle(recordRequest.getRecordTitle())
            .recordLatitude(recordRequest.getRecordLatitude())
            .recordLongitude(recordRequest.getRecordLongitude())
            .recordPath(recordUrl)
            .recordIsSelected(false)
            .recordArea(1L)
            .build();
        return recordRepository.save(record).getId();
    }

    @Override
    public List<RecordResponse> findSelectedRecord(Long departmentStoreId, Long departmentStoreFloorId) {
        List<RecordResponse> recordList = recordRepository.findSelectedRecordsByDepartmentStoreIdAndFloor(departmentStoreId, departmentStoreFloorId)
                .stream()
                .map(record -> new RecordResponse(
                        record.getRecordId(),
                        record.getRecordTitle(),
                        record.getRecordLatitude(),
                        record.getRecordLongitude(),
                        record.getRecordPath()
                ))
                .collect(Collectors.toList());
        return recordList;

    }

}
