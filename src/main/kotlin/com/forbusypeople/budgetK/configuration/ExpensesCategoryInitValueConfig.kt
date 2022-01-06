package com.forbusypeople.budgetK.configuration

import com.forbusypeople.budgetK.repository.ExpensesCategoryEntity
import com.forbusypeople.budgetK.repository.ExpensesCategoryRepository
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ExpensesCategoryInitValueConfig(
    private val expensesCategoryRepository: ExpensesCategoryRepository
) {

    @EventListener(ApplicationReadyEvent::class)
    fun saveBasicCategory() {
        val category = expensesCategoryRepository.findAll()
        if (category.isEmpty()) {
            expensesCategoryRepository.saveAll(listOf(
                ExpensesCategoryEntity(name = "FOR_LIFE"),
                ExpensesCategoryEntity(name = "EDUCATION"),
                ExpensesCategoryEntity(name = "FUN"),
                ExpensesCategoryEntity(name = "OTHERS"),
            ))
        }
    }

}