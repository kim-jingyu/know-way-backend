package com.knowway.departmentstore.service;

import com.knowway.departmentstore.dto.DepartmentStoreFloorMapResponse;
import com.knowway.departmentstore.dto.DepartmentStoreRequest;
import com.knowway.departmentstore.dto.DepartmentStoreResponse;
import com.knowway.departmentstore.entity.DepartmentStore;
import com.knowway.departmentstore.entity.DepartmentStoreFloor;
import com.knowway.departmentstore.exception.DepartmentStoreNotFoundException;
import com.knowway.departmentstore.repository.DepartmentStoreFloorRepository;
import com.knowway.departmentstore.repository.DepartmentStoreRepository;
import com.knowway.s3.exception.S3Exception;
import com.knowway.s3.service.S3UploadService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DepartmentStoreServiceImpl implements DepartmentStoreService {
    private final DepartmentStoreRepository departmentStoreRepository;
    private final DepartmentStoreFloorRepository departmentStoreFloorRepository;
    private final S3UploadService s3UploadService;

    private static final double DISTANCE = 50.0;
    private static final double EARTH_RADIUS = 6371.0;

    @Transactional
    @Override
    public Long makeDepartmentStore(DepartmentStoreRequest request) {
        return departmentStoreRepository.save(DepartmentStore.createDepartmentStore(request, request.getFloorData().stream()
                .map(request1 -> {
                    MultipartFile image = request1.getImage();
                    String imageUrl = null;
                    try {
                        imageUrl = s3UploadService.saveFile(image, "image");
                    } catch (IOException e) {
                        throw new S3Exception();
                    }
                    return departmentStoreFloorRepository.save(DepartmentStoreFloor.createDepartmentStoreFloor(request1.getDepartmentStoreFloor(), imageUrl));
                })
                .toList())).getDepartmentStoreId();
    }

    @Override
    public Page<DepartmentStoreResponse> getAllDepartmentStoreList(Integer size, Integer page) {
        return departmentStoreRepository.findAll(PageRequest.of(page, size))
                .map(DepartmentStoreResponse::of);
    }

    @Override
    public List<DepartmentStoreResponse> getDepartmentStoreListByLocation(Double latitude, Double longtitude) {
        List<StoreDistance> nearByStores = new ArrayList<>();
        for (DepartmentStore departmentStore : departmentStoreRepository.findAll()) {
            double distance = calculateDistance(latitude, longtitude, departmentStore.getDepartmentStoreLatitude(), departmentStore.getDepartmentStoreLongtitude());
            if (distance <= DISTANCE) {
                nearByStores.add(StoreDistance.builder()
                        .distance(distance)
                        .response(DepartmentStoreResponse.of(departmentStore))
                        .build());
            }
        }
        nearByStores.sort(Comparator.comparingDouble(StoreDistance::distance));
        return nearByStores.stream()
                .map(StoreDistance::response)
                .toList();
    }

    @Override
    public DepartmentStoreFloorMapResponse getDepartmentStoreFloorMap(Long deptId, String floor) {
        return DepartmentStoreFloorMapResponse.of(departmentStoreFloorRepository.findByDepartmentStoreAndDepartmentStoreFloor(departmentStoreRepository.getById(deptId), floor));
    }

    @Override
    public List<DepartmentStoreFloorMapResponse> getDepartmentStoreFloorList(Long departmentStoreId) {
        return departmentStoreRepository.getById(departmentStoreId).getDepartmentStoreFloorList().stream()
                .map(DepartmentStoreFloorMapResponse::of)
                .toList();
    }

    @Override
    public DepartmentStoreResponse getDepartmentStoreByBranch(String departmentStoreBranch) {
        return DepartmentStoreResponse.of(departmentStoreRepository.findByDepartmentStoreBranch(departmentStoreBranch)
                .orElseThrow(DepartmentStoreNotFoundException::new));
    }

    @Transactional
    @Override
    public void removeDepartmentStore(Long departmentStoreId) {
        departmentStoreRepository.delete(departmentStoreRepository.getById(departmentStoreId));
    }

    @Builder
    private record StoreDistance(double distance, DepartmentStoreResponse response) {
    }

    private double calculateDistance(double latitude1, double longtitude1, double latitude2, double longtitude2) {
        double la1Rad = Math.toRadians(latitude1);
        double long1Rad = Math.toRadians(longtitude1);
        double la2Rad = Math.toRadians(latitude2);
        double long2Rad = Math.toRadians(longtitude2);

        double latDiff = la2Rad - la1Rad;
        double longDiff = long2Rad - long1Rad;

        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) + Math.cos(la1Rad) * Math.cos(la2Rad) * Math.sin(longDiff / 2) * Math.sin(longDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}
