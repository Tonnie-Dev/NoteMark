package com.tonyxlab.notemark.presentation.core.utils

import android.content.Context
import androidx.annotation.StringRes
import com.tonyxlab.notemark.R

sealed class SupportText(@StringRes val defaultText: Int, @StringRes val errorText: Int) {

    data object BlankSupportText : SupportText(
            defaultText = R.string.blank_text,
            errorText =R.string.blank_text,
    )

    data object UsernameSupportText : SupportText(
            defaultText = R.string.sup_text_def_username,
            errorText = R.string.sup_text_error_username
    )

    data object EmailSupportText : SupportText(
            defaultText = R.string.blank_text,
            errorText = R.string.sup_text_error_email
    )


    data object PasswordOneSupportText : SupportText(
            defaultText = R.string.sup_text_def_password_one,
            errorText = R.string.sup_text_error_password_one
    )

    data object PasswordTwoSupportText : SupportText(
            defaultText = R.string.blank_text,
            errorText = R.string.sup_text_error_password_two
    )


}


fun SupportText.getDefaultText(context: Context): String = context.getString(defaultText)
fun SupportText.getErrorText(context: Context): String = context.getString(errorText)

