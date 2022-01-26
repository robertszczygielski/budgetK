package com.forbusypeople.budgetK.services

import com.forbusypeople.budgetK.repository.ExpenseEntity
import com.forbusypeople.budgetK.repository.ExpensesCategoryEntity
import com.forbusypeople.budgetK.repository.ExpensesCategoryRepository
import com.forbusypeople.budgetK.repository.ExpensesRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Instant
import java.util.*

class ExpenseServiceImplTest {

    @Test
    internal fun shouldGetAllExpensesAndMapFromEntityToDto() {
        // given
        val expectedAmount = BigDecimal.TEN
        val expectedCategory = "myCategory"
        val categoryId = UUID.randomUUID()

        val allEntity = listOf(
            ExpenseEntity(
                UUID.randomUUID(),
                expectedAmount,
                "myDesc",
                Instant.now(),
                categoryId
            )
        )

        val categoryEntity = ExpensesCategoryEntity(
            categoryId,
            expectedCategory
        )

        val expensesRepository = mockk<ExpensesRepository>()
        val expensesCategoryRepository = mockk<ExpensesCategoryRepository>()

        every { expensesRepository.findAll() } returns allEntity
        every { expensesCategoryRepository.findById(categoryId) } returns Optional.of(categoryEntity)

        val expenseService = ExpenseServiceImpl(expensesRepository, expensesCategoryRepository)

        // when
        val allDto = expenseService.getAll()

        // then
        assertAll(
            { assertEquals(allDto.get(0).amount, expectedAmount) },
            { assertEquals(allDto.get(0).expensesCategory, expectedCategory) }
        )

    }

    @Test
    internal fun shouldGetAllExpensesAndMapFromEntityToDtoByCategoryName() {
        // given
        val expectedAmount = BigDecimal.TEN
        val expectedCategory = "myCategory"
        val categoryId = UUID.randomUUID()

        val allEntity = listOf(
            ExpenseEntity(
                UUID.randomUUID(),
                expectedAmount,
                "myDesc",
                Instant.now(),
                categoryId
            )
        )

        val categoryEntity = ExpensesCategoryEntity(
            categoryId,
            expectedCategory
        )

        val expensesRepository = mockk<ExpensesRepository>()
        val expensesCategoryRepository = mockk<ExpensesCategoryRepository>()

        every { expensesRepository.findByExpensesCategory(categoryId) } returns allEntity
        every { expensesCategoryRepository.findByName(expectedCategory.uppercase()) } returns listOf(categoryEntity)
        every { expensesCategoryRepository.findById(categoryId) } returns Optional.of(categoryEntity)

        val expenseService = ExpenseServiceImpl(expensesRepository, expensesCategoryRepository)

        // when
        val allDto = expenseService.getExpensesByCategory(expectedCategory)

        // then
        assertAll(
            { assertEquals(allDto.get(0).amount, expectedAmount) },
            { assertEquals(allDto.get(0).expensesCategory, expectedCategory) }
        )

    }

    @Test
    internal fun shouldReturnEmptyListIfThereAreNoExpensesByCategory() {
        // given
        val expectedCategory = "myCategory"

        val expensesRepository = mockk<ExpensesRepository>()
        val expensesCategoryRepository = mockk<ExpensesCategoryRepository>()

        every { expensesCategoryRepository.findByName(expectedCategory.uppercase()) } returns emptyList()

        val expenseService = ExpenseServiceImpl(expensesRepository, expensesCategoryRepository)

        // when
        val allDto = expenseService.getExpensesByCategory(expectedCategory)

        // then
        assertEquals(allDto.size, 0)

    }
}