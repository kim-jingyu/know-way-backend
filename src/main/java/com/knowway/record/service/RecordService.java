package com.knowway.record.service;

import com.knowway.image.exception.S3Exception;
import com.knowway.image.service.S3UploadService;
import com.knowway.record.dto.RecordDto;
import com.knowway.record.entity.Record;
import com.knowway.record.repository.RecordRepository;
import com.knowway.user.entity.Member;
import com.knowway.user.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class RecordService {

    private final RecordRepository recordRepository;
    private final MemberRepository memberRepository;
    private final S3UploadService s3UploadService;

    @Transactional
    public Long addRecord(RecordDto recordDto, MultipartFile recordFile) {
        String recordUrl = null;
        try {
            recordUrl = s3UploadService.saveFile(recordFile);
        } catch (IOException e) {
            throw new S3Exception();
        }

        Member member = memberRepository.findById(recordDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        Record record = Record.builder()
                .member(member)
                .departmentStoreFloorId(recordDto.getDepartmentStoreFloorId())
                .departmentStoreId(recordDto.getDepartmentStoreId())
                .recordTitle(recordDto.getRecordTitle())
                .recordLatitude(recordDto.getRecordLatitude())
                .recordLongitude(recordDto.getRecordLongitude())
                .recordPath(recordUrl)
                .recordIsSelected(false)
                .build();

        return recordRepository.save(record).getId();
    }
}
