package com.recommendation.project.pharmacy.service

import com.recommendation.project.AbstractIntegrationContainerBaseTest
import com.recommendation.project.pharmacy.entity.Pharmacy
import com.recommendation.project.pharmacy.repository.PharmacyRepository
import org.springframework.beans.factory.annotation.Autowired


class PharmacyRepositoryServiceTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private PharmacyRepositoryService pharmacyRepositoryService

    @Autowired
    private PharmacyRepository pharmacyRepository

    def setup() {
        pharmacyRepository.deleteAll()
    }

    def "PharmacyRepository update - JPA dirty checking success"() {
        given:
        String inputAddress = "서울 특별시 성북구 종압동"
        String modifiedAddress = "서울 광진구 구의동"
        String name = "은혜 약국"

        def pharmacy = Pharmacy.builder()
                .pharmacyAddress(inputAddress)
                .pharmacyName(name)
                .build()

        when:
        def entity = pharmacyRepository.save(pharmacy)
        pharmacyRepositoryService.updateAddress(entity.getId(), modifiedAddress)

        def results = pharmacyRepository.findAll()

        then:
        results.get(0).getPharmacyAddress() == modifiedAddress
    }

    def "PharmacyRepository update - JPA dirty checking failure"() {
        given:
        String inputAddress = "서울 특별시 성북구 종압동"
        String modifiedAddress = "서울 광진구 구의동"
        String name = "은혜 약국"

        def pharmacy = Pharmacy.builder()
                .pharmacyAddress(inputAddress)
                .pharmacyName(name)
                .build()

        when:
        def entity = pharmacyRepository.save(pharmacy)
        pharmacyRepositoryService.updateAddressWithoutTransactional(entity.getId(), modifiedAddress)

        def results = pharmacyRepository.findAll()

        then:
        results.get(0).getPharmacyAddress() == inputAddress
    }
}