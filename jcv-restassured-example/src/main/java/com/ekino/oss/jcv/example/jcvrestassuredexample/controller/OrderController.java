/*
 * Copyright (c) 2019 ekino (https://www.ekino.com/)
 */
package com.ekino.oss.jcv.example.jcvrestassuredexample.controller;

import com.ekino.oss.jcv.example.jcvrestassuredexample.dto.OrderDto;
import com.ekino.oss.jcv.example.jcvrestassuredexample.dto.OrderItemDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RequestMapping(path = "/orders")
@RestController
public class OrderController {

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Object getOrderById(@PathVariable UUID id) {
        return generateOrderMock(id);
    }

    private static OrderDto generateOrderMock(UUID id) {
        return OrderDto.builder()
            .id(id)
            .reference("SOME_REF")
            .createdDate(Instant.now())
            .shippingDate(ZonedDateTime.of(
                LocalDateTime.of(2019, 4, 23, 10, 0),
                ZoneId.of("Europe/Paris")
            ))
            .items(generateItems())
            .build();
    }

    private static List<OrderItemDto> generateItems() {
        return Stream.of(
            OrderItemDto.builder()
                .id(UUID.fromString("05170f42-7972-4552-9f0d-334567109984"))
                .reference("ITEM_1")
                .build(),
            OrderItemDto.builder()
                .id(UUID.randomUUID())
                .reference("ITEM_2")
                .build(),
            OrderItemDto.builder()
                .id(UUID.fromString("d6ecd6a5-ae6b-4eda-a944-e78f788011c4"))
                .reference("ITEM_9999")
                .build()
        )
            .collect(toList());
    }
}
