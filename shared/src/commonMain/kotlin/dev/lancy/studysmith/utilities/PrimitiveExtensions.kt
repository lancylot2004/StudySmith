package dev.lancy.studysmith.utilities

inline fun <reified T> Boolean.fold(
    ifTrue: T,
    ifFalse: T,
) = if (this) { ifTrue } else { ifFalse }

inline fun <reified T> List<T>.oxfordJoin(
    separator: String = ", ",
    lastSeparator: String = "and",
    placeholder: String = "",
    prefix: String = "",
    suffix: String = "",
): String = buildString {
    append(prefix)
    when (size) {
        0 -> append(placeholder)
        1 -> append(this@oxfordJoin.first().toString())
        2 -> {
            append(this@oxfordJoin.first())
            append(lastSeparator)
            append(this@oxfordJoin.last())
        }
        else -> {
            this@oxfordJoin.dropLast(1).forEach {
                append(it.toString())
                append(separator)
            }
            append(lastSeparator)
            append(" ")
            append(this@oxfordJoin.last())
        }
    }
    append(suffix)
}
