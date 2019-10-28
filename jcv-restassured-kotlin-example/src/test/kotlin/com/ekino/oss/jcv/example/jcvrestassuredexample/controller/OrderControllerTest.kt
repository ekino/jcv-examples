/*
 * Copyright (c) 2019 ekino (https://www.ekino.com/)
 */
package com.ekino.oss.jcv.example.jcvrestassuredexample.controller

import com.ekino.oss.jcv.assertion.hamcrest.JsonMatcherBuilder
import com.ekino.oss.jcv.assertion.hamcrest.JsonMatchers.jsonMatcher
import com.ekino.oss.jcv.core.JsonValueComparator
import com.ekino.oss.jcv.core.comparator.DateTimeFormatComparator
import com.ekino.oss.jcv.core.validator.Validators.defaultValidators
import com.ekino.oss.jcv.core.validator.validators
import io.restassured.RestAssured.given
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.skyscreamer.jsonassert.JSONCompareMode
import org.skyscreamer.jsonassert.ValueMatcherException
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.nio.file.Paths
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest {

    companion object {

        private val DEFAULT_ORDER_BY_ID_PATH = Paths
            .get("/orders", "555cb3a5-74a6-4e33-854a-9b8c7c712b92")
            .toString()
    }

    @LocalServerPort
    private val serverPort: Int = 0

    @DisplayName("Default : Test non-extensible body without ordering in arrays")
    @Test
    fun `should get order by id`() {

        given()
            .port(serverPort)
            .get(DEFAULT_ORDER_BY_ID_PATH)
            .then()
            .statusCode(HttpStatus.OK.value())
            .body(
                jsonMatcher(
                    // language=json
                    """
                    {
                      "id": "555cb3a5-74a6-4e33-854a-9b8c7c712b92",
                      "reference": "SOME_REF",
                      "createdDate": "{#date_time_format:iso_instant#}",
                      "shippingDate": "2019-04-23T10:00:00+02:00",
                      "items": [
                        {
                          "id": "d6ecd6a5-ae6b-4eda-a944-e78f788011c4",
                          "reference": "{#regex:^ITEM_[0-9]+$#}"
                        },
                        {
                          "id": "05170f42-7972-4552-9f0d-334567109984",
                          "reference": "ITEM_1"
                        },
                        {
                          "id": "{#uuid#}",
                          "reference": "ITEM_2"
                        }
                      ]
                    }
                    """.trimIndent()
                )
            )
    }

    @DisplayName("Test non-extensible body with strict ordering in arrays")
    @Test
    fun `should get order by id sorted`() {

        given()
            .port(serverPort)
            .get(DEFAULT_ORDER_BY_ID_PATH)
            .then()
            .statusCode(HttpStatus.OK.value())
            .body(
                JsonMatcherBuilder.create()
                    .mode(JSONCompareMode.STRICT)
                    .build(
                        // language=json
                        """
                        {
                          "id": "555cb3a5-74a6-4e33-854a-9b8c7c712b92",
                          "reference": "SOME_REF",
                          "createdDate": "{#date_time_format:iso_instant#}",
                          "shippingDate": "2019-04-23T10:00:00+02:00",
                          "items": [
                            {
                              "id": "05170f42-7972-4552-9f0d-334567109984",
                              "reference": "ITEM_1"
                            },
                            {
                              "id": "{#uuid#}",
                              "reference": "ITEM_2"
                            },
                            {
                              "id": "d6ecd6a5-ae6b-4eda-a944-e78f788011c4",
                              "reference": "{#starts_with:ITEM_#}"
                            }
                          ]
                        }
                        """.trimIndent()
                    )
            )
    }

    @DisplayName("Test extensible body")
    @Test
    fun `should get order by id light`() {

        given()
            .port(serverPort)
            .get(DEFAULT_ORDER_BY_ID_PATH)
            .then()
            .statusCode(HttpStatus.OK.value())
            .body(
                JsonMatcherBuilder.create()
                    .mode(JSONCompareMode.LENIENT)
                    .build(
                        // language=json
                        """
                        {
                          "id": "{#uuid#}",
                          "reference": "SOME_REF",
                          "shippingDate": "{#date_time_format:iso_offset_date_time#}",
                          "items": "{#array_type#}"
                        }
                        """.trimIndent()
                    )
            )
    }

    @DisplayName("Test extensible body with custom matcher")
    @Test
    fun `should get order by id custom matcher`() {

        given()
            .port(serverPort)
            .get(DEFAULT_ORDER_BY_ID_PATH)
            .then()
            .statusCode(HttpStatus.OK.value())
            .body(
                JsonMatcherBuilder.create()
                    .mode(JSONCompareMode.LENIENT)
                    .validators(validators {
                        +defaultValidators()
                        +templatedValidator<String>("order_shipping_date_min") {
                            allOf {
                                +DateTimeFormatComparator(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                                +comparatorWith1RequiredParameter { DateMinYearComparator(it.toInt()) }
                            }
                        }
                    })
                    .build(
                        // language=json
                        """
                        {
                          "id": "{#uuid#}",
                          "reference": "SOME_REF",
                          "shippingDate": "{#order_shipping_date_min:2019#}"
                        }
                        """.trimIndent()
                    )
            )
    }
}

class DateMinYearComparator(private val minYear: Int) : JsonValueComparator<String> {

    override fun hasCorrectValue(actual: String?, expected: String?): Boolean {

        val dateTime = OffsetDateTime.parse(actual)

        if (minYear <= dateTime.year) {
            return true
        }
        throw ValueMatcherException("Date year should be equal or after year $minYear", expected, actual)
    }
}
