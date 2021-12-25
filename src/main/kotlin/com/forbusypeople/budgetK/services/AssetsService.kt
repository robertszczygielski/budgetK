package com.forbusypeople.budgetK.services

import com.forbusypeople.budgetK.controllers.AssetDto
import org.springframework.stereotype.Service

interface AssetsService {
    fun getAssets(): List<AssetDto>
    fun saveAssets(dtoList: List<AssetDto>)
}

@Service
class AssetServiceImpl(
    private var dots: List<AssetDto>
): AssetsService {
    override fun getAssets(): List<AssetDto> = dots

    override fun saveAssets(dtoList: List<AssetDto>) {
        this.dots = dtoList
    }

}