package com.knowway.point.service;

import com.knowway.point.dto.PointRequest;
import com.knowway.point.entity.Point;
import com.knowway.point.exception.PointException;
import com.knowway.point.repository.PointRepository;
import com.knowway.point.service.PointService;
import com.knowway.record.entity.Record;
import com.knowway.record.repository.RecordRepository;
import com.knowway.user.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class PointServiceImpl implements PointService {
    private final PointRepository pointRepository;
    private final RecordRepository recordRepository;

    @Override
    public void addPoint(PointRequest pointRequest) {
        Long recordId = pointRequest.getRecordId();
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new PointException("record id 값이 올바르지 않습니다."));
        Member member = record.getMember();

        Point point = Point.builder()
                .member(member)
                .build();

        pointRepository.save(point);
    }
}
