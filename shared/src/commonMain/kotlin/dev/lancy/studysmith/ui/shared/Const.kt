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
    val Small = 23.dp

    val Medium = 36.dp

    val Large = 48.dp
}

object Shape {
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

object Const {
    val HazeStyle
        @Composable get() = HazeStyle(ColourScheme.background.copy(alpha = 0.3f), Padding.Small, 0.2f)
}

object Animation {
    fun <T> short(): AnimationSpec<T> = tween(50, 0)

    fun <T> delayedShort(): AnimationSpec<T> = tween(100, 50)

    fun <T> delayedMedium(): AnimationSpec<T> = tween(400, 60)
}

val Typography: Typography
    @Composable get() = MaterialTheme.typography

val ColourScheme: ColorScheme
    @Composable get() = MaterialTheme.colorScheme
