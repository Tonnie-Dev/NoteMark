package com.tonyxlab.notemark.presentation.core.utils


fun isValidUsername(username: CharSequence): Boolean = username.length in 3..20

fun isValidEmail(email: CharSequence?): Boolean {
    return !email.isNullOrBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
            .matches()
}

fun isValidPassword(input: CharSequence): Boolean {

    val isLongEnough = input.length >= 8
    val hasNumberOrSymbol = input.any { it.isDigit() || !it.isLetterOrDigit() }

    return isLongEnough && hasNumberOrSymbol
}

fun isValidLoginPassword(input: CharSequence): Boolean = input.isNotBlank()

infix fun CharSequence.isSameAs(other: CharSequence): Boolean = this.trim()==other.trim()

fun allFieldsFilled(vararg fields: CharSequence): Boolean = fields.all { it.isNotBlank() }


fun checkIfError(input: CharSequence, validator: (CharSequence)-> Boolean): Boolean {

    return input.isNotBlank() && !validator(input)
}

