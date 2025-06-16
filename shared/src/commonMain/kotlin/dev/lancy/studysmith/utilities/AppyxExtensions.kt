package dev.lancy.studysmith.utilities

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.bumble.appyx.components.spotlight.Spotlight
import dev.lancy.studysmith.ui.shared.NavTarget

@Composable
fun <T : NavTarget> Spotlight<T>.selectedIndex(): Int =
    this.activeIndex
        .collectAsState()
        .value
        .toInt()
