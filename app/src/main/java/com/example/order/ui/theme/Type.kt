package com.example.order.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.app.R

val cabinFontFamily = FontFamily(
    Font(R.font.cabin_bold, weight = FontWeight.Bold)
)

val metropolisFontFamily = FontFamily(
    Font(R.font.metropolis_regular, weight = FontWeight.Normal),
    Font(R.font.metropolis_medium, weight = FontWeight.Medium),
    Font(R.font.metropolis_semi_bold, weight = FontWeight.SemiBold),
    Font(R.font.metropolis_bold, weight = FontWeight.Bold),
    Font(R.font.metropolis_extra_bold, weight = FontWeight.ExtraBold),
)

val Typography = Typography(
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)