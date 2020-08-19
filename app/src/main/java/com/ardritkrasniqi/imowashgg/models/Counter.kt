package com.ardritkrasniqi.imowashgg.models

data class Counter(
    val express: Int = 0,
    val felge_2: Int = 0,
    val felge_3: Int = 0
) {
    override fun toString(): String {
        return "Exp: $express, Felge2: $felge_2, Felge3: $felge_3"
    }
}