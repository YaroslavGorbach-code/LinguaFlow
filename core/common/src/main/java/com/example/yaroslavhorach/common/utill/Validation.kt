package com.example.yaroslavhorach.common.utill

import com.example.yaroslavhorach.ui.UiText


object Validation {

    /**
     * perform validation and return its result (Boolean) and [String] if it failed.
     */
    fun validate(textValidation: TextValidation, input: String): Pair<Boolean, UiText> {

        textValidation.rules.forEach { pair ->
            val errorMsg = pair.second
            val isValid = when (pair.first) {
                Rule.RequiredField -> Validators.validateRequiredField(input)
                Rule.DefaultMinLength -> Validators.validateMinLength(minLength = textValidation.minLength, input)
                Rule.DefaultMaxLength -> Validators.validateMaxLength(maxLength = textValidation.maxLength, input)
            }

            if (!isValid) {
                return false to errorMsg
            }
        }

        return true to UiText.Empty
    }

    object Validators {
        fun validateRequiredField(input: String): Boolean = input.isNotEmpty() && input.isNotBlank()

        fun validateMinLength(minLength: Int, input: String): Boolean = input.length >= minLength

        fun validateMaxLength(maxLength: Int, input: String): Boolean = input.length <= maxLength
    }

    enum class Rule {
        RequiredField,
        DefaultMinLength,
        DefaultMaxLength
    }
}

sealed class ValidationState {
    data object Idle : ValidationState()
    data object Success : ValidationState()
    class Error(val errorText: UiText) : ValidationState()
}

@Suppress("ArrayInDataClass")
data class TextValidation(
    val state: ValidationState = ValidationState.Idle,
    val rules: Array<Pair<Validation.Rule, UiText>> = emptyArray(),
    val minLength: Int = 0,
    val maxLength: Int = Int.MAX_VALUE
)