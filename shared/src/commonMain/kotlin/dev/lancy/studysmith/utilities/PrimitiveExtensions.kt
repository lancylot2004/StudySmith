package dev.lancy.studysmith.utilities

inline fun <reified T> Boolean.fold(
    ifTrue: T,
    ifFalse: T,
) = if (this) { ifTrue } else { ifFalse }

/**
 * Joins a list of items into a string with an Oxford comma format.
 */
inline fun <reified T> List<T>.oxfordJoin(
    separator: String = ", ",
    lastSeparator: String = "and",
    placeholder: String = "",
    prefix: String = "",
    suffix: String = "",
): String = buildString {
    if (isEmpty()) {
        append(placeholder)
        return@buildString
    }

    append(prefix)
    when (size) {
        1 -> append(first())
        2 -> {
            append(first())
            append(" $lastSeparator ")
            append(last())
        }
        else -> {
            this@oxfordJoin.dropLast(1).joinTo(this, separator) { it.toString() }
            append(" $lastSeparator ")
            append(last())
        }
    }
    append(suffix)
}
