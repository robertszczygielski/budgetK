package com.forbusypeople.budgetK.services

import com.forbusypeople.budgetK.controllers.AssetDto
import org.springframework.stereotype.Service

interface AssetsService {
    fun getAssets(): List<AssetDto>
}

@Service
class AssetServiceImpl: AssetsService {
    override fun getAssets(): List<AssetDto> = listOf(
        AssetDto(1),
        AssetDto(4)
    )

}