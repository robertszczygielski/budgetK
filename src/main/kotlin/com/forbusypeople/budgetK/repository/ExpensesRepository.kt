package com.forbusypeople.budgetK.repository

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import java.math.BigDecimal
import java.time.Instant
import java.util.*

interface ExpensesRepository: MongoRepository<ExpenseEntity, UUID> {

    fun findByExpensesCategory(expensesCategory: UUID): List<ExpenseEntity>

}

interface ExpensesCategoryRepository: MongoRepository<ExpensesCategoryEntity, UUID> {

    fun findByName(name: String): List<ExpensesCategoryEntity>

}

@Document("Expense")
data class ExpenseEntity(
    @Id
    val id: UUID = UUID.randomUUID(),
    val amount: BigDecimal,
    val description: String,
    val purchaseDate: Instant,
    val expensesCategory: UUID,
)

@Document("ExpensesCategory")
data class ExpensesCategoryEntity(
    @Id
    val id: UUID = UUID.randomUUID(),
    val name: String,
)