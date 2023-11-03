package com.myudak.allymotion.util

import androidx.annotation.DrawableRes
import com.myudak.allymotion.R

sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
    val title: String,
    val description: String
) {
    object First : OnBoardingPage(
        image = R.drawable.illustration1,
        title = "Olahrga dari mana saja!",
        description = "Olahraga dapat dilakukan dimana saja dan kapan saja. Maka, lakukanlah sekarang juga!"
    )

    object Second : OnBoardingPage(
        image = R.drawable.illustration2,
        title = "Seimbangkan Makanan",
        description = "Makanan yang sehat mencerminkan tubuh yang kuat"
    )

    object Third : OnBoardingPage(
        image = R.drawable.ic_logo__bener_,
        title = "AllyMotion",
        description = "Mulai pola hidup sehatmu dengan AllyMotion bersamamu."
    )
}