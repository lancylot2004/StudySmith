package dev.lancy.studysmith.structures

data class SessionConfiguration(
    val useAppBlocker: Boolean = true,
    val useWebBlocker: Boolean = false,
    val useDoNotDisturb: Boolean = false,
)
