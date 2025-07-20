package com.example.lunchtray.ui

import androidx.annotation.StringRes
import com.example.lunchtray.R

enum class Screen(
    @StringRes val stringId: Int
) {
    Start(R.string.start_order),
    EntreeMenu(R.string.choose_entree),
    SideDishMenu(R.string.choose_side_dish),
    AccompanimentMenu(R.string.choose_accompaniment),
    Checkout(R.string.order_checkout)
}