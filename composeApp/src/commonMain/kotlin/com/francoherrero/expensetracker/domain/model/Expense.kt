package com.francoherrero.expensetracker.domain.model

import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class Expense @OptIn(ExperimentalUuidApi::class) constructor(
    val id: String,
    val title: String,
    val money: Money,
    val category: String?,
    val notes: String?,
    val createdAt: Instant,
    val updatedAt: Instant
)

@Serializable
data class Money(val amountCents: Long, val currency: String)