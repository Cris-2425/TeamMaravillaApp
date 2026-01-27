package com.example.teammaravillaapp.model

import androidx.annotation.StringRes
import com.example.teammaravillaapp.R

enum class ListViewType(@StringRes val labelRes: Int) {
    BUBBLES(R.string.list_view_bubbles),
    LIST(R.string.list_view_list),
    COMPACT(R.string.list_view_compact)
}