package com.forbusypeople.budgetK.services

import com.forbusypeople.budgetK.repository.ExpenseEntity
import com.forbusypeople.budgetK.repository.ExpensesCategoryRepository
import com.forbusypeople.budgetK.repository.ExpensesRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.Instant
import java.util.*

interface ExpenseService {
    fun saveAll(dtoList: List<ExpenseDto>)
}

@Service
class ExpenseServiceImpl(
    private val expensesRepository: ExpensesRepository,
    private val expensesCategoryRepository: ExpensesCategoryRepository
): ExpenseService {
    override fun saveAll(dtoList: List<ExpenseDto>) {
        val allCategory = expensesCategoryRepository.findAll()
        expensesRepository.saveAll(
            dtoList.map {
                it.toEntity(allCategory.find { category -> category.name == it.expensesCategory }?.id
                    ?: UUID.fromString("11111111-1111-1111-1111-111111111111")
                )
            }
        )
    }
}

private fun ExpenseDto.toEntity(categoryId: UUID) = ExpenseEntity(
    amount = this.amount,
    description = this.description,
    purchaseDate = this.purchaseDate,
    expensesCategory = categoryId
)

// TODO: Move to Controller;
data class ExpenseDto(
    val id: UUID?,
    val amount: BigDecimal,
    val description: String,
    val purchaseDate: Instant,
    val expensesCategory: String,
)
