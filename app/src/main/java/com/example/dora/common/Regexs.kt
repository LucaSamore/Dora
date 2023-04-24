package com.example.dora.common

object Regexs {

    val emailAddress =
        ("^[\\\\w!#\$%&amp;'*+/=?`{|}~^-]+(?:\\\\.[\\\\w!" +
                "#\$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,6}\$").toRegex()

    val password = "\"^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}\$\"".toRegex()
}