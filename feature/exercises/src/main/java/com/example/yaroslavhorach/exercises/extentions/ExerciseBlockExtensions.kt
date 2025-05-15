package com.example.yaroslavhorach.exercises.extentions

import com.example.yaroslavhorach.domain.exercise.model.ExerciseBlock

val ExerciseBlock.topBarBgRes: Int
    get() = when (this) {
        ExerciseBlock.ONE -> com.example.yaroslavhorach.designsystem.R.drawable.gradient_orange
        ExerciseBlock.TWO -> com.example.yaroslavhorach.designsystem.R.drawable.gradient_orange
        ExerciseBlock.THREE -> com.example.yaroslavhorach.designsystem.R.drawable.gradient_orange
        ExerciseBlock.FOUR -> com.example.yaroslavhorach.designsystem.R.drawable.gradient_orange
        ExerciseBlock.FIVE -> com.example.yaroslavhorach.designsystem.R.drawable.gradient_orange
        ExerciseBlock.SIX -> com.example.yaroslavhorach.designsystem.R.drawable.gradient_orange
    }