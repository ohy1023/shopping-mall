package store.myproject.onlineshop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import store.myproject.onlineshop.domain.dto.brand.*;
import store.myproject.onlineshop.domain.entity.Brand;
import store.myproject.onlineshop.exception.AppException;
import store.myproject.onlineshop.exception.ErrorCode;
import store.myproject.onlineshop.global.s3.service.AwsS3Service;
import store.myproject.onlineshop.global.utils.FileUtils;
import store.myproject.onlineshop.repository.BrandRepository;

import java.util.List;
import java.util.stream.Collectors;

import static store.myproject.onlineshop.exception.ErrorCode.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    private final AwsS3Service awsS3Service;

    // 브랜드 단건 조회
    public BrandInfo getBrandInfo(Long id) {
        return getBrandOrElseThrow(id).toBrandInfo();
    }

    // 브랜드 전체 조회
    public List<BrandInfo> getBrandInfos() {
        return brandRepository.findAll().stream()
                .map((Brand::toBrandInfo))
                .toList();
    }

    // 브랜드 추가
    public BrandCreateResponse saveBrand(BrandCreateRequest request, MultipartFile multipartFile) {

        log.info("request : {}", request.toString());
        log.info("Check if file exists : {}", !multipartFile.isEmpty());

        checkDuplicatedBrand(request.getName());

        String originImageUrl = awsS3Service.uploadBrandOriginImage(multipartFile);

        request.setOriginImagePath(originImageUrl);

        Brand savedBrand = brandRepository.save(request.toEntity());

        return savedBrand.toBrandCreateResponse();
    }

    // 브랜드 수정
    public BrandUpdateResponse updateBrand(Long id, BrandUpdateRequest request, MultipartFile multipartFile) {

        Brand brand = getBrandOrElseThrow(id);

        log.info("Check if file exists : {}", !multipartFile.isEmpty());

        if (!multipartFile.isEmpty()) {
            String extractFileName = FileUtils.extractFileName(brand.getOriginImagePath());

            awsS3Service.deleteBrandImage(extractFileName);

            String newUrl = awsS3Service.uploadBrandOriginImage(multipartFile);

            request.setOriginImagePath(newUrl);
        } else {
            request.setOriginImagePath(brand.getOriginImagePath());
        }

        brand.update(request);

        return brand.toBrandUpdateResponse();
    }

    // 브랜드 삭제
    public BrandDeleteResponse deleteBrand(Long id) {

        Brand brand = getBrandOrElseThrow(id);

        String extractFileName = FileUtils.extractFileName(brand.getOriginImagePath());

        awsS3Service.deleteBrandImage(extractFileName);

        brandRepository.deleteById(id);

        return brand.toBrandDeleteResponse();
    }


    // 브랜드 찾기
    private Brand getBrandOrElseThrow(Long id) {
        return brandRepository.findById(id).orElseThrow(() -> new AppException(BRAND_NOT_FOUND, BRAND_NOT_FOUND.getMessage()));
    }

    // 브랜드 명 중복 확인
    private void checkDuplicatedBrand(String brandName) {
        if (brandRepository.existsByName(brandName)) {
            throw new AppException(DUPLICATE_BRAND);
        }
    }


}
