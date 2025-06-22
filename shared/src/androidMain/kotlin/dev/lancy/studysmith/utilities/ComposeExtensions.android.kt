package dev.lancy.studysmith.utilities

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@Composable
@Suppress("unused")
actual fun getScreenSize(): DpSize = with(LocalConfiguration.current) {
    DpSize(this.screenWidthDp.dp, this.screenHeightDp.dp)
}
