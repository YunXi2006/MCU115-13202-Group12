package com.example.foodorderingwizard

data class Order(
    val mainMeal: String? = null,
    val sideDishes: List<String> = emptyList(),
    val drink: String? = null
) {
    fun isComplete() = mainMeal != null && sideDishes.isNotEmpty() && drink != null
}
