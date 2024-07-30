package com.knowway.record.service;

import com.knowway.s3.exception.S3Exception;
import com.knowway.s3.service.S3UploadService;
import com.knowway.record.dto.RecordRequest;
import com.knowway.record.entity.Record;
import com.knowway.record.repository.RecordRepository;
import com.knowway.user.entity.Member;
import com.knowway.user.exception.UserException;
import com.knowway.user.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class RecordServiceImpl implements RecordService {

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

        Member member = memberRepository.findById(recordRequest.getMemberId())
                .orElseThrow(() -> new UserException("유효하지 않은 아이디입니다."));

        Record record = Record.builder()
                .member(member)
                .departmentStoreFloorId(recordRequest.getDepartmentStoreFloorId())
                .departmentStoreId(recordRequest.getDepartmentStoreId())
                .recordTitle(recordRequest.getRecordTitle())
                .recordLatitude(recordRequest.getRecordLatitude())
                .recordLongitude(recordRequest.getRecordLongitude())
                .recordPath(recordUrl)
                .recordIsSelected(false)
                .build();

        return recordRepository.save(record).getId();
    }
}
