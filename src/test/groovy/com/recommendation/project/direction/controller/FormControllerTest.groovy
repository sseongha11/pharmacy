package com.recommendation.project.direction.controller

import com.recommendation.project.direction.dto.InputDto
import com.recommendation.project.direction.dto.OutputDto
import com.recommendation.project.pharmacy.service.PharmacyRecommendationService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class FormControllerTest extends Specification {

    private MockMvc mockMvc
    private PharmacyRecommendationService pharmacyRecommendationService = Mock()
    private List<OutputDto> outputDtoList

    def setup() {
        // Create a mock MVC for FormController
        mockMvc = MockMvcBuilders.standaloneSetup(new FormController(pharmacyRecommendationService)).build()

        // Create a list of OutputDto
        outputDtoList = new ArrayList<>()
        outputDtoList.addAll(
                OutputDto.builder()
                        .pharmacyName("pharmacy1")
                        .build(),
                OutputDto.builder()
                        .pharmacyName("pharmacy2")
                        .build()
        )
    }

    def "GET /"() {
        expect:
        // When the user sends a GET request ("/") to "main" URL
        mockMvc.perform(get("/"))
                .andExpect(handler().handlerType(FormController.class))
                .andExpect(handler().methodName("main"))
                .andExpect(view().name("main"))
                .andExpect(status().isOk())
                .andDo(log())
    }

    def "POST /search"() {

        given:
        String inputAddress = "서울 성북구 종암동"
        InputDto inputDto = new InputDto(inputAddress)

        when:
        def resultActions = mockMvc.perform(post("/search")
                .flashAttr("inputDto", inputDto))

        then:
        1 * pharmacyRecommendationService.recommendPharmacyList(argument -> {
            assert argument == inputAddress // Check if the input address is the same as the argument
        }) >> outputDtoList

        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("output"))
                .andExpect(model().attributeExists("outputFormList"))
                .andExpect(model().attribute("outputFormList", outputDtoList))
                .andDo(print())
    }
}