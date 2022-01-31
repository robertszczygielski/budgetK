package com.forbusypeople.budgetK.services

import com.forbusypeople.budgetK.controllers.ExpenseDto
import com.forbusypeople.budgetK.controllers.ExpensesCategoryDto
import com.forbusypeople.budgetK.repository.ExpenseEntity
import com.forbusypeople.budgetK.repository.ExpensesCategoryEntity
import com.forbusypeople.budgetK.repository.ExpensesCategoryRepository
import com.forbusypeople.budgetK.repository.ExpensesRepository
import org.springframework.stereotype.Service
import java.util.*

interface ExpenseService {
    fun saveAll(dtoList: List<ExpenseDto>)
    fun getAll(): List<ExpenseDto>
    fun getExpensesByCategory(name: String): List<ExpenseDto>
    fun deleteExpense(id: UUID)
}

interface ExpensesCategoryService{
    fun getAll(): List<ExpensesCategoryDto>
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

    override fun getAll(): List<ExpenseDto> = expensesRepository.findAll()
        .map {
            it.toDto(
                expensesCategoryRepository.findById(it.expensesCategory).get().name
            )
        }

    override fun getExpensesByCategory(name: String) =
        expensesCategoryRepository.findByName(name.uppercase())
            .firstOrNull()
            ?.let {
                expensesRepository.findByExpensesCategory(
                    it.id
                )
                    .map { entity ->
                        entity.toDto(
                            expensesCategoryRepository.findById(entity.expensesCategory).get().name
                        )
                    }
            }.orEmpty()

    override fun deleteExpense(id: UUID) = expensesRepository.deleteById(id)

}

@Service
class ExpensesCategoryServiceImpl(
    private val expensesCategoryRepository: ExpensesCategoryRepository
): ExpensesCategoryService {
    override fun getAll(): List<ExpensesCategoryDto> = expensesCategoryRepository.findAll()
        .map { it.toDto() }
}

private fun ExpensesCategoryEntity.toDto() = ExpensesCategoryDto(
    id = this.id,
    name = this.name
)


private fun ExpenseEntity.toDto(categoryName: String) = ExpenseDto(
    id = this.id,
    amount = this.amount,
    description = this.description,
    purchaseDate = this.purchaseDate,
    expensesCategory = categoryName
)

private fun ExpenseDto.toEntity(categoryId: UUID) = ExpenseEntity(
    amount = this.amount,
    description = this.description,
    purchaseDate = this.purchaseDate,
    expensesCategory = categoryId
)
