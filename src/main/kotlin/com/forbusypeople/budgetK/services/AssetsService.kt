package com.forbusypeople.budgetK.services

import com.forbusypeople.budgetK.controllers.AssetDto
import com.forbusypeople.budgetK.controllers.AssetsSumWithPercentDto
import com.forbusypeople.budgetK.controllers.SumAssetsAmountDto
import com.forbusypeople.budgetK.repository.AssetEntity
import com.forbusypeople.budgetK.repository.AssetsRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

interface AssetsService {
    fun getAssets(): List<AssetDto>
    fun saveAssets(dtoList: List<AssetDto>)
    fun deleteAsset(id: UUID)
    fun updateAsset(dto: AssetDto)
    fun getAllSumOfAssetsByCategory(): SumAssetsAmountDto
}

@Service
class AssetServiceImpl(
    private val assetsRepository: AssetsRepository
) : AssetsService {
    override fun getAssets(): List<AssetDto> = assetsRepository.findAll()
        .map { it.toDto() }

    override fun saveAssets(dtoList: List<AssetDto>) {
        assetsRepository.saveAll(dtoList.map { it.toEntity() })
    }

    override fun deleteAsset(id: UUID) = assetsRepository.deleteById(id)

    override fun updateAsset(dto: AssetDto) {
        val entity = assetsRepository.findById(dto.id!!).get()
        assetsRepository.save(entity.updateByDto(dto))
    }

    override fun getAllSumOfAssetsByCategory(): SumAssetsAmountDto {
        val sumForCategory = getOnlySumForCategory()
        val sumAll = sumForCategory.map { it.value }.sumOf { it }

        val categorySumWithPercent = sumForCategory.entries
            .groupBy(
                { it.key },
                { AssetsSumWithPercentDto(it.value, percentForCategory(it.value, sumAll)) }
            ).map { it.key to it.value.first() }.toMap()

        return SumAssetsAmountDto(categorySumWithPercent)
    }

    private fun percentForCategory(value: BigDecimal, sumAll: BigDecimal): Float =
        (value.multiply(BigDecimal(100))/sumAll).toFloat()

    private fun getOnlySumForCategory() = assetsRepository.findAll()
        .groupBy { it.category.name }
        .mapValues { (_, asset) -> asset.sumOf { it.amount } }

}

private fun AssetEntity.updateByDto(dto: AssetDto): AssetEntity {
    this.category = dto.category
    this.amount = dto.amount
    this.description = dto.description
    this.incomeDate = dto.incomeDate

    return this
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
