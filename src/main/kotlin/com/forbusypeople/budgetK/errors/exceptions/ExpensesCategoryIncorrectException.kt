package com.forbusypeople.budgetK.errors.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

class ExpensesCategoryIncorrectException(message: String): RuntimeException(message)

class ExpensesAmountToLongException(message: String): RuntimeException(message)

class ExpensesDescriptionToShortException(message: String): RuntimeException(message)

@RestControllerAdvice
class ExpensesCategoryExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    fun expensesHandlerCategoryError(exc: ExpensesCategoryIncorrectException) = BudgetError(exc.message!!)

    @ExceptionHandler
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    fun expensesHandlerAmountToLongError(exc: ExpensesAmountToLongException) = BudgetError(exc.message!!)

    @ExceptionHandler
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    fun expensesHandlerDescriptionToShortError(exc: ExpensesDescriptionToShortException) = BudgetError(exc.message!!)

}

data class BudgetError(
    val description: String
)