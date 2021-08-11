package com.ottarson.hackerreader.ui.newslist

import com.ottarson.hackerreader.R

enum class SortOption(
    val menuItem: Int,
    val displayName: String,
    val apiEndpoint: String,
    val order: Int
) {
    Top(R.id.menu_sort_option_top, "Top", "topstories.json", 0),
    New(R.id.menu_sort_option_new, "New", "newstories.json", 1),
    Best(R.id.menu_sort_option_best, "Best", "beststories.json", 2)
}
