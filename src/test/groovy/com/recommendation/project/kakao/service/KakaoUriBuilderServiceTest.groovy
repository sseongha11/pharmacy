package com.recommendation.project.kakao.service


import spock.lang.Specification

import java.nio.charset.StandardCharsets

class KakaoUriBuilderServiceTest extends Specification {
    private KakaoUriBuilderService kakaoUriBuilderService

    def setup() {
        kakaoUriBuilderService = new KakaoUriBuilderService()
    }

    def "shouldCorrectlyEncodeKoreanParametersInUri"() {
        given:
        def address = "서울 성북구"
        def charset = StandardCharsets.UTF_8

        when:
        def uri = kakaoUriBuilderService.buildUriByAddressSearch(address)
        def decodedResults =  URLDecoder.decode(uri.toString(), charset)

        then:
        decodedResults == "https://dapi.kakao.com/v2/local/search/address.json?query=서울 성북구"
    }

}