package com.francoherrero.expensetracker

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform