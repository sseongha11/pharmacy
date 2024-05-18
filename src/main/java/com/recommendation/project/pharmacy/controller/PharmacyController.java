package com.recommendation.project.pharmacy.controller;

import com.recommendation.project.pharmacy.cache.PharmacyRedisTemplateService;
import com.recommendation.project.pharmacy.dto.PharmacyDto;
import com.recommendation.project.pharmacy.entity.Pharmacy;
import com.recommendation.project.pharmacy.service.PharmacyRepositoryService;
import com.recommendation.project.util.CsvUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PharmacyController {

    private final PharmacyRepositoryService pharmacyRepositoryService;
    private final PharmacyRedisTemplateService pharmacyRedisTemplateService;

    // The saveCsv method saves the CSV data to the database and Redis cache.
    @GetMapping("/csv/save")
    public String saveCsv() {
        saveCsvToDatabase();
//        saveCsvToRedis();

        return "success save";
    }

    public void saveCsvToDatabase() {

        List<Pharmacy> pharmacyList = loadPharmacyList();
        pharmacyRepositoryService.saveAll(pharmacyList);

    }

    public void saveCsvToRedis() {

        List<PharmacyDto> pharmacyDtoList = pharmacyRepositoryService.findAll()
                .stream().map(pharmacy -> PharmacyDto.builder()
                        .id(pharmacy.getId())
                        .pharmacyName(pharmacy.getPharmacyName())
                        .pharmacyAddress(pharmacy.getPharmacyAddress())
                        .latitude(pharmacy.getLatitude())
                        .longitude(pharmacy.getLongitude())
                        .build())
                .toList();

        pharmacyDtoList.forEach(pharmacyRedisTemplateService::save);

    }

    private List<Pharmacy> loadPharmacyList() {
        return CsvUtils.convertToPharmacyDtoList()
                .stream().map(pharmacyDto ->
                        Pharmacy.builder()
                                .id(pharmacyDto.getId())
                                .pharmacyName(pharmacyDto.getPharmacyName())
                                .pharmacyAddress(pharmacyDto.getPharmacyAddress())
                                .latitude(pharmacyDto.getLatitude())
                                .longitude(pharmacyDto.getLongitude())
                                .build())
                .collect(Collectors.toList());
    }
}
