package com.knowway.record.service;

import com.knowway.departmentstore.entity.DepartmentStore;
import com.knowway.departmentstore.entity.DepartmentStoreFloor;
import com.knowway.departmentstore.repository.DepartmentStoreFloorRepository;
import com.knowway.departmentstore.repository.DepartmentStoreRepository;
import com.knowway.record.dto.RecordRequest;
import com.knowway.record.dto.RecordResponse;
import com.knowway.record.entity.Record;
import com.knowway.record.exception.CurrentRecordLocationNotValidException;
import com.knowway.record.repository.RecordRepository;
import com.knowway.s3.exception.S3Exception;
import com.knowway.s3.service.S3UploadService;
import com.knowway.user.entity.Member;
import com.knowway.user.exception.UserNotFoundException;
import com.knowway.user.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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

    private static final List<double[]> POINTS = List.of(
            new double[]{37.58383691083842, 127.00000000000001},
            new double[]{37.58375582142429, 127.00006226890987},
            new double[]{37.583776093765515, 126.99992357904445},
            new double[]{37.58370176184163, 127.00000000000001},
            new double[]{37.58371077168309, 126.999852819029},
            new double[]{37.58363643981044, 126.99994056159007}
    );

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }

    private Optional<Long> getArea(double latitude, double longitude) {
        for (int i = 0; i < POINTS.size(); i++) {
            double[] point = POINTS.get(i);
            if (calculateDistance(latitude, longitude, point[0], point[1]) <= 10) {
                return Optional.of((long) (i + 1));
            }
        }
        return Optional.empty();
    }

    @Transactional
    public Long addRecord(Long memberId, RecordRequest recordRequest, MultipartFile recordFile) {
        String recordUrl = null;
        try {
            recordUrl = s3UploadService.saveFile(recordFile, "record");
        } catch (IOException e) {
            throw new S3Exception();
        }
        DepartmentStoreFloor departmentStoreFloor = storeFloorRepository.getById(
            recordRequest.getDepartmentStoreFloorId());

        DepartmentStore departmentStore = departmentStoreRepository.getById(recordRequest.getDepartmentStoreId());
        Member member = memberRepository.findById(memberId)
            .orElseThrow(UserNotFoundException::new);

        double latitude = Double.parseDouble(recordRequest.getRecordLatitude());
        double longitude = Double.parseDouble(recordRequest.getRecordLongitude());
        Long area = getArea(latitude, longitude).orElseThrow(
            CurrentRecordLocationNotValidException::new);

        Record record = Record.builder()
            .member(member)
            .departmentStore(departmentStore)
            .departmentStoreFloor(departmentStoreFloor)
            .recordTitle(recordRequest.getRecordTitle())
            .recordLatitude(recordRequest.getRecordLatitude())
            .recordLongitude(recordRequest.getRecordLongitude())
            .recordPath(recordUrl)
            .recordIsSelected(false)
            .recordArea(area)
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
