package dev.lancy.studysmith.ui.shared

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeStyle

object Padding {
    val Small = 4.dp

    val Medium = 8.dp

    val Large = 16.dp
}

object Size {
    val Small = 28.dp

    val Medium = 36.dp

    val Large = 48.dp
}

object Rounded {
    val Small = RoundedCornerShape(10)

    val Medium = RoundedCornerShape(33)

    val Large = RoundedCornerShape(75)

    val Full = RoundedCornerShape(100)
}

object RoundedPercent {
    const val SMALL: Int = 10

    const val MEDIUM: Int = 33

    const val LARGE = 75

    const val FULL = 100
}

object Haze {
    val Dark
        @Composable get() = HazeStyle(ColourScheme.background.copy(alpha = 0.3f), Padding.Small, 0.2f)

    val Primary
        @Composable get() = HazeStyle(ColourScheme.primary.copy(alpha = 0.3f), Padding.Small, 0f)
}

object Animation {
    fun <T> short(): AnimationSpec<T> = tween(100, 0)

    fun <T> medium(): AnimationSpec<T> = tween(300, 0)

    fun <T> long(): AnimationSpec<T> = tween(500, 0)
}

val Typography: Typography
    @Composable get() = MaterialTheme.typography

val ColourScheme: ColorScheme
    @Composable get() = MaterialTheme.colorScheme
