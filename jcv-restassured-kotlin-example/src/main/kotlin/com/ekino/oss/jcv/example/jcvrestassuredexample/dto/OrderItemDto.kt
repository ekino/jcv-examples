/*
 * Copyright (c) 2019 ekino (https://www.ekino.com/)
 */
package com.ekino.oss.jcv.example.jcvrestassuredexample.dto

import java.util.UUID

data class OrderItemDto(
    var id: UUID? = null,
    var reference: String? = null
)
