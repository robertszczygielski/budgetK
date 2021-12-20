package com.forbusypeople.budgetK.controllers

import com.forbusypeople.budgetK.services.AssetsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/assets")
class AssetsController(
    val assetsService: AssetsService
) {

    @GetMapping
    fun getAllAssets() = assetsService.getAssets()

}

data class AssetDto(
    val amount: Int
)