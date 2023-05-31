package com.example.dora.common

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun nowWithPattern(pattern: String): String =
  LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern))
