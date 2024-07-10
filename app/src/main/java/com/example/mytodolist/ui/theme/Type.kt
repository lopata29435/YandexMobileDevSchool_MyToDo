package com.example.mytodolist.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object MyTypography {
    @Composable
    fun largeTitle() = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        lineHeight = 37.5.sp,
        fontSize = 32.sp,
        color = LocalColors.current.labelPrimary
    )

    @Composable
    fun title() = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        lineHeight = 32.sp,
        fontSize = 20.sp,
        color = LocalColors.current.labelPrimary
    )

    @Composable
    fun headline() = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        lineHeight = 24.sp,
        fontSize = 14.sp,
        color = LocalColors.current.labelPrimary
    )

    @Composable
    fun body() = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W400,
        lineHeight = 20.sp,
        fontSize = 16.sp,
        color = LocalColors.current.labelPrimary
    )

    @Composable
    fun subhead() = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W400,
        lineHeight = 20.sp,
        fontSize = 14.sp,
        color = LocalColors.current.labelPrimary
    )

    @Composable
    fun footnote() = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W600,
        lineHeight = 18.sp,
        fontSize = 13.sp,
        color = LocalColors.current.labelPrimary
    )
}