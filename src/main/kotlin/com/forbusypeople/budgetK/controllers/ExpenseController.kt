package com.forbusypeople.budgetK.controllers

import com.forbusypeople.budgetK.services.ExpenseService
import com.forbusypeople.budgetK.services.ExpensesCategoryService
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/expenses")
class ExpenseController(
    private val expenseService: ExpenseService
) {

    @GetMapping
    fun getAll() = expenseService.getAll()

    @GetMapping("/sum/category")
    fun getAllExpensesSumByCategory() = expenseService.getAllExpensesSumByCategory()

    @PostMapping
    fun saveAll(@RequestBody dtoList: List<ExpenseDto>) = expenseService.saveAll(dtoList)

    @GetMapping("/filter")
    fun getExpensesByCategory(@RequestParam("category") name: String) = expenseService.getExpensesByCategory(name)

    @DeleteMapping("/{id}")
    fun deleteExpenses(@PathVariable("id") id: UUID) = expenseService.deleteExpense(id)

    @PutMapping
    fun updateExpense(@RequestBody dto: ExpenseDto) = expenseService.updateExpense(dto)

}

@RestController
@RequestMapping("/expenses/category")
class ExpensesCategoryController(
    private val expensesCategoryService: ExpensesCategoryService
) {

    @GetMapping
    fun getAll() = expensesCategoryService.getAll()
}

data class ExpenseDto(
    val id: UUID?,
    val amount: BigDecimal,
    val description: String,
    val purchaseDate: Instant,
    val expensesCategory: String,
)

data class ExpensesCategoryDto(
    val id: UUID?,
    val name: String,
)

data class ExpensesSumByCategoryDto(
    val sum: Map<String, BigDecimal>
)

