package com.forbusypeople.budgetK.services

import com.forbusypeople.budgetK.controllers.ExpenseDto
import com.forbusypeople.budgetK.controllers.ExpensesCategoryDto
import com.forbusypeople.budgetK.controllers.ExpensesSumByCategoryDto
import com.forbusypeople.budgetK.errors.exceptions.ExpensesAmountToLongException
import com.forbusypeople.budgetK.errors.exceptions.ExpensesCategoryIncorrectException
import com.forbusypeople.budgetK.errors.exceptions.ExpensesDescriptionToShortException
import com.forbusypeople.budgetK.repository.ExpenseEntity
import com.forbusypeople.budgetK.repository.ExpensesCategoryEntity
import com.forbusypeople.budgetK.repository.ExpensesCategoryRepository
import com.forbusypeople.budgetK.repository.ExpensesRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

interface ExpenseService {
    fun saveAll(dtoList: List<ExpenseDto>)
    fun getAll(): List<ExpenseDto>
    fun getExpensesByCategory(name: String): List<ExpenseDto>
    fun deleteExpense(id: UUID)
    fun updateExpense(dto: ExpenseDto)
    fun getAllExpensesSumByCategory(): ExpensesSumByCategoryDto
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

    override fun updateExpense(dto: ExpenseDto) {
        val newCategory = expensesCategoryRepository
            .findByName(dto.expensesCategory)
            .firstOrNull()?.id
            ?: categoryFail(dto.expensesCategory)

        when {
            dto.amount < BigDecimal.ZERO -> amountToLongFail(dto.amount)
            dto.description.length < 3 -> descriptionToShortFail()
        }

        val expense = expensesRepository.findById(dto.id!!).get()
        val newExpense = expense.copy(
            amount = dto.amount,
            description = dto.description,
            expensesCategory = newCategory,
            purchaseDate = dto.purchaseDate
        )

        expensesRepository.save(newExpense)

    }

    override fun getAllExpensesSumByCategory(): ExpensesSumByCategoryDto {
        val allCategory = expensesCategoryRepository.findAll()
        val expensesSum = allCategory.groupBy(
            { it.name },
            { expensesRepository.findByExpensesCategory(it.id).sumOf { expense -> expense.amount } }
        ).map { it.key to it.value.first() }.toMap()

        return ExpensesSumByCategoryDto(expensesSum)
    }

    private fun categoryFail(categoryName: String): Nothing =
        throw ExpensesCategoryIncorrectException("Incorrect category $categoryName")

    private fun amountToLongFail(amount: BigDecimal): Nothing =
        throw ExpensesAmountToLongException("Incorrect amount $amount")

    private fun descriptionToShortFail(): Nothing =
        throw ExpensesDescriptionToShortException("Description is to short")
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
