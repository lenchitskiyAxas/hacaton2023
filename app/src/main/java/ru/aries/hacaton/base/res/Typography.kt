package ru.aries.hacaton.base.res

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import ru.aries.hacaton.R
import ru.aries.hacaton.base.theme.TypographySchemeApp


val TypographyApp = TypographySchemeApp(
    titleLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.nunito_sans_semi_bold)),
        fontSize = 24.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.nunito_sans_regular)),
        fontSize = 20.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.nunito_sans_bold)),
        fontSize = 16.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.nunito_sans_regular)),
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.nunito_sans_regular)),
        fontSize = 14.sp
    ),
    button = TextStyle(
        fontFamily = FontFamily(Font(R.font.nunito_sans_semi_bold)),
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily(Font(R.font.nunito_sans_regular)),
        fontSize = 12.sp
    ),
    label = TextStyle(
        fontFamily = FontFamily(Font(R.font.nunito_sans_extra_bold)),
        fontSize = 12.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.nunito_sans_extra_bold)),
        fontSize = 12.sp
    )
)
