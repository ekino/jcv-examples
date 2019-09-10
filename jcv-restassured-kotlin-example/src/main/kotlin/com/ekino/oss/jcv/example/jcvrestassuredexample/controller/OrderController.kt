/*
 * Copyright (c) 2019 ekino (https://www.ekino.com/)
 */
package com.ekino.oss.jcv.example.jcvrestassuredexample.controller

import com.ekino.oss.jcv.example.jcvrestassuredexample.dto.OrderDto
import com.ekino.oss.jcv.example.jcvrestassuredexample.dto.OrderItemDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

@RequestMapping(path = ["/orders"])
@RestController
class OrderController {

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getOrderById(@PathVariable id: UUID) = generateOrderMock(id)

    private fun generateOrderMock(id: UUID) = OrderDto(
        id = id,
        reference = "SOME_REF",
        createdDate = Instant.now(),
        shippingDate = ZonedDateTime.of(
            LocalDateTime.of(2019, 4, 23, 10, 0),
            ZoneId.of("Europe/Paris")
        ),
        items = generateItems()
    )

    private fun generateItems() = listOf(
        OrderItemDto(
            id = UUID.fromString("05170f42-7972-4552-9f0d-334567109984"),
            reference = "ITEM_1"
        ),
        OrderItemDto(
            id = UUID.randomUUID(),
            reference = "ITEM_2"
        ),
        OrderItemDto(
            id = UUID.fromString("d6ecd6a5-ae6b-4eda-a944-e78f788011c4"),
            reference = "ITEM_9999"
        )
    )
}
