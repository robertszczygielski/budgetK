package com.forbusypeople.budgetK.repository

import com.forbusypeople.budgetK.enums.AssetCategory
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import java.math.BigDecimal
import java.time.Instant
import java.util.*

interface AssetsRepository: MongoRepository<AssetEntity, UUID>

@Document("Asset")
data class AssetEntity(
    @Id
    val id: UUID = UUID.randomUUID(),
    val amount: BigDecimal,
    val category: AssetCategory,
    val incomeDate: Instant,
    val description: String
)