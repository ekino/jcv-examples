/*
 * Copyright (c) 2019 ekino (https://www.ekino.com/)
 */
package com.ekino.oss.jcv.example.jcvrestassuredexample.dto

import java.time.Instant
import java.time.ZonedDateTime
import java.util.UUID

data class OrderDto(
    var id: UUID? = null,
    var reference: String? = null,
    var createdDate: Instant? = null,
    var shippingDate: ZonedDateTime? = null,
    var items: List<OrderItemDto> = emptyList()
)
