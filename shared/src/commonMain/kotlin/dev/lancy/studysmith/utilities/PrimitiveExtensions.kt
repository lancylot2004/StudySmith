package dev.lancy.studysmith.utilities

import kotlin.contracts.ExperimentalContracts

@OptIn(ExperimentalContracts::class)
inline fun <reified T> Boolean.fold(
    ifTrue: T,
    ifFalse: T,
) = if (this) { ifTrue } else { ifFalse }
