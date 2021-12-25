package com.forbusypeople.budgetK.controllers

import com.forbusypeople.budgetK.enums.AssetCategory
import com.forbusypeople.budgetK.services.AssetsService
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/assets")
class AssetsController(
    private val assetsService: AssetsService
) {

    @GetMapping
    fun getAllAssets() = assetsService.getAssets()

    @PostMapping
    fun saveAllAssets(@RequestBody dtoList: List<AssetDto>) = assetsService.saveAssets(dtoList)

}

data class AssetDto(
    val id: UUID?,
    val amount: BigDecimal,
    val category: AssetCategory,
    val incomeDate: Instant,
    val description: String
)