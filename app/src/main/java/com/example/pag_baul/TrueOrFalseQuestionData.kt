// In new file: TrueOrFalseQuestionData.kt
package com.example.pag_baul

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrueOrFalseQuestionData(
    val question: String,
    val answer: String
) : Parcelable
