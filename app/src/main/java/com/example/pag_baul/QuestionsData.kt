package com.example.pag_baul

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestionData(
    val question: String,
    val option1: String,
    val option2: String,
    val option3: String,
    val option4: String, // Can be empty string "" if only 3 choices
    val answer: String,
) : Parcelable
