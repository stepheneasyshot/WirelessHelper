package com.stephen.wirelesshelper

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform