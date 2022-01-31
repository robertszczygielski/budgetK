package com.forbusypeople.budgetK.services

import com.forbusypeople.budgetK.controllers.AssetDto
import com.forbusypeople.budgetK.repository.AssetEntity
import com.forbusypeople.budgetK.repository.AssetsRepository
import org.springframework.stereotype.Service
import java.util.*

interface AssetsService {
    fun getAssets(): List<AssetDto>
    fun saveAssets(dtoList: List<AssetDto>)
    fun deleteAsset(id: UUID)
}

@Service
class AssetServiceImpl(
    private val assetsRepository: AssetsRepository
): AssetsService {
    override fun getAssets(): List<AssetDto> = assetsRepository.findAll()
        .map{ it.toDto() }

    override fun saveAssets(dtoList: List<AssetDto>) {
        assetsRepository.saveAll(dtoList.map { it.toEntity() })
    }

    override fun deleteAsset(id: UUID) = assetsRepository.deleteById(id)

}

private fun AssetDto.toEntity() = AssetEntity(
    amount = this.amount,
    category = this.category,
    incomeDate = this.incomeDate,
    description = this.description
)

private fun AssetEntity.toDto() = AssetDto(
    id = this.id,
    amount = this.amount,
    category = this.category,
    incomeDate = this.incomeDate,
    description = this.description
)
