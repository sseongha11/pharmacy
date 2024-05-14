package com.recommendation.project.kakao.service

import com.recommendation.project.AbstractIntegrationContainerBaseTest
import org.springframework.beans.factory.annotation.Autowired


class KakaoAddressSearchServiceTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private KakaoAddressSearchService kakaoAddressSearchService

    def "shouldReturnNullWhenAddressParameterIsNull"() {
        given:
        String address = null

        when:
        def result = kakaoAddressSearchService.requestAddressSearch(address)

        then:
        result == null
    }

    def "shouldReturnValidDocumentWhenAddressIsValid"() {
        given:
        def address = "서울특별시 성북구 종암동 10길"

        when:
        def result = kakaoAddressSearchService.requestAddressSearch(address)

        then:
        result.documentList.size() > 0
        result.metaDto.totalCount > 0
        result.documentList[0].addressName != null
    }

    def "should return longitude and latitude when address is valid"() {
        given:
        boolean actualResult = false

        when:
        def searchResult =  kakaoAddressSearchService.requestAddressSearch(inputAddress)

        then:
        if(searchResult == null) {
            actualResult = false
        } else {
            actualResult = searchResult.getDocumentList().size() > 0
        }

        where:
        inputAddress                            | expectedResult
        "서울 특별시 성북구 종암동"                   | true
        "서울 성북구 종암동 91"                     | true
        "서울 대학로"                             | true
        "서울 성북구 종암동 잘못된 주소"               | false
        "광진구 구의동 251-45"                     | true
        "광진구 구의동 251-455555"                 | false
        ""                                      | false
    }
}