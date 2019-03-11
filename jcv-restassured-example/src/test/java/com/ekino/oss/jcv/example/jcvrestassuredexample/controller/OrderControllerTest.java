/*
 * Copyright (c) 2019 ekino (https://www.ekino.com/)
 */
package com.ekino.oss.jcv.example.jcvrestassuredexample.controller;

import com.ekino.oss.jcv.assertion.hamcrest.JsonMatcherBuilder;
import com.ekino.oss.jcv.core.JsonValidator;
import com.ekino.oss.jcv.core.JsonValueComparator;
import com.ekino.oss.jcv.core.comparator.DateTimeFormatComparator;
import com.ekino.oss.jcv.core.validator.Validators;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.ValueMatcherException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import static com.ekino.oss.jcv.assertion.hamcrest.JsonMatchers.*;
import static com.ekino.oss.jcv.core.initializer.Initializers.*;
import static io.restassured.RestAssured.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest {

    private static final String DEFAULT_ORDER_BY_ID_PATH = Paths
        .get("/orders", "555cb3a5-74a6-4e33-854a-9b8c7c712b92")
        .toString();

    @LocalServerPort
    private Integer serverPort;

    @DisplayName("Default : Test non-extensible body without ordering in arrays")
    @Test
    void shouldGetOrderById() {

        given()
            .port(serverPort)
            .get(DEFAULT_ORDER_BY_ID_PATH)
            .then()
            .statusCode(HttpStatus.OK.value())
            .body(jsonMatcher(loadJson("get_order_by_id_expected.json")));
    }

    @DisplayName("Test non-extensible body with strict ordering in arrays")
    @Test
    void shouldGetOrderByIdSorted() {

        given()
            .port(serverPort)
            .get(DEFAULT_ORDER_BY_ID_PATH)
            .then()
            .statusCode(HttpStatus.OK.value())
            .body(JsonMatcherBuilder.create()
                .mode(JSONCompareMode.STRICT)
                .build(loadJson("get_order_by_id_sorted_expected.json"))
            );
    }

    @DisplayName("Test extensible body")
    @Test
    void shouldGetOrderByIdLight() {

        given()
            .port(serverPort)
            .get(DEFAULT_ORDER_BY_ID_PATH)
            .then()
            .statusCode(HttpStatus.OK.value())
            .body(JsonMatcherBuilder.create()
                .mode(JSONCompareMode.LENIENT)
                .build(loadJson("get_order_by_id_light_expected.json"))
            );
    }

    @DisplayName("Test extensible body with custom matcher")
    @Test
    void shouldGetOrderByIdCustomMatcher() {

        given()
            .port(serverPort)
            .get(DEFAULT_ORDER_BY_ID_PATH)
            .then()
            .statusCode(HttpStatus.OK.value())
            .body(JsonMatcherBuilder.create()
                .mode(JSONCompareMode.LENIENT)
                .validators(customValidators())
                .build(loadJson("get_order_by_id_custom_matcher_expected.json"))
            );
    }

    private static List<JsonValidator> customValidators() {
        LinkedList<JsonValidator> validators = new LinkedList<>();

        validators.add(orderShippingDateValidator());
        validators.addAll(Validators.defaultValidators());

        return validators;
    }

    private static JsonValidator orderShippingDateValidator() {
        return parameterizedValidator(
            "order_shipping_date_min",
            allOf(
                comparatorWithoutParameter(() -> new DateTimeFormatComparator(DateTimeFormatter.ISO_OFFSET_DATE_TIME)),
                comparatorWith1Parameter(minYear -> new DateMinYearComparator(Integer.parseInt(minYear)))
            )
        );
    }

    private static class DateMinYearComparator implements JsonValueComparator<String> {

        private final Integer minYear;

        private DateMinYearComparator(Integer minYear) {
            this.minYear = minYear;
        }

        @Override
        public boolean hasCorrectValue(String actual, String expected) {

            OffsetDateTime dateTime = OffsetDateTime.parse(actual);

            if (minYear <= dateTime.getYear()) {
                return true;
            }
            throw new ValueMatcherException("Date year should be equal or after year " + minYear, expected, actual);
        }
    }

    private static String loadJson(String filename) {
        try {
            return IOUtils.resourceToString(
                Paths.get("/controller/orders", filename).toString(),
                StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
