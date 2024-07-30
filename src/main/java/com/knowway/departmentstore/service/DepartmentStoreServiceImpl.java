package com.knowway.departmentstore.service;

import com.knowway.departmentstore.entity.DepartmentStore;
import com.knowway.departmentstore.entity.DepartmentStoreFloor;
import com.knowway.departmentstore.dto.DepartmentStoreFloorMapResponse;
import com.knowway.departmentstore.dto.DepartmentStoreRequest;
import com.knowway.departmentstore.dto.DepartmentStoreResponse;
import com.knowway.departmentstore.exception.DepartmentStoreNotFoundException;
import com.knowway.departmentstore.repository.DepartmentStoreFloorRepository;
import com.knowway.departmentstore.repository.DepartmentStoreRepository;
import com.knowway.s3.exception.S3Exception;
import com.knowway.s3.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DepartmentStoreServiceImpl implements DepartmentStoreService {
    private final DepartmentStoreRepository departmentStoreRepository;
    private final DepartmentStoreFloorRepository departmentStoreFloorRepository;
    private final S3UploadService s3UploadService;

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
    public Page<DepartmentStoreResponse> getDepartmentStoreListByLocation(Integer size, Integer page, String latitude, String longtitude) {
        return departmentStoreRepository.findPageByLatitudeLongtitude(latitude, longtitude, PageRequest.of(page, size))
                .map(DepartmentStoreResponse::of);
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
}
