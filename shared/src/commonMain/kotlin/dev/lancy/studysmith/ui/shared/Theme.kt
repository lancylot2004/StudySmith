package dev.lancy.drp25.ui.shared

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dev.lancy.studysmith.utilities.fold

internal val lightColourScheme = lightColorScheme(
    primary = Color(0xFF006A66),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF4CBDB7),
    onPrimaryContainer = Color(0xFF004946),
    secondary = Color(0xFF3F6562),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFBFE8E4),
    onSecondaryContainer = Color(0xFF436967),
    tertiary = Color(0xFF714F94),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFC39DE8),
    onTertiaryContainer = Color(0xFF523173),
    error = Color(0xFFA23541),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFC34D58),
    onErrorContainer = Color(0xFFFFFBFF),
    background = Color(0xFFF5FAF9),
    onBackground = Color(0xFF171D1C),
    surface = Color(0xFFF5FAF9),
    onSurface = Color(0xFF171D1C),
    surfaceVariant = Color(0xFFD8E5E3),
    onSurfaceVariant = Color(0xFF3D4948),
    outline = Color(0xFF6D7A78),
    outlineVariant = Color(0xFFBCC9C7),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFF2C3131),
    inverseOnSurface = Color(0xFFEDF2F0),
    inversePrimary = Color(0xFF6AD8D1),
    surfaceDim = Color(0xFFD6DBDA),
    surfaceBright = Color(0xFFF5FAF9),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF0F5F3),
    surfaceContainer = Color(0xFFEAEFED),
    surfaceContainerHigh = Color(0xFFE4E9E8),
    surfaceContainerHighest = Color(0xFFDEE3E2),
)

internal val darkColourScheme = darkColorScheme(
    primary = Color(0xFF6BD9D3),
    onPrimary = Color(0xFF003735),
    primaryContainer = Color(0xFF4CBDB7),
    onPrimaryContainer = Color(0xFF004946),
    secondary = Color(0xFFA6CECB),
    onSecondary = Color(0xFF0D3634),
    secondaryContainer = Color(0xFF294F4D),
    onSecondaryContainer = Color(0xFF98C0BD),
    tertiary = Color(0xFFDDBAFF),
    onTertiary = Color(0xFF411F62),
    tertiaryContainer = Color(0xFFC39DE8),
    onTertiaryContainer = Color(0xFF523173),
    error = Color(0xFFFFB3B5),
    onError = Color(0xFF660419),
    errorContainer = Color(0xFFE66972),
    onErrorContainer = Color(0xFF250004),
    background = Color(0xFF0F1414),
    onBackground = Color(0xFFDEE3E2),
    surface = Color(0xFF0F1414),
    onSurface = Color(0xFFDEE3E2),
    surfaceVariant = Color(0xFF3D4948),
    onSurfaceVariant = Color(0xFFBCC9C7),
    outline = Color(0xFF879392),
    outlineVariant = Color(0xFF3D4948),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFFDEE3E2),
    inverseOnSurface = Color(0xFF2C3131),
    inversePrimary = Color(0xFF006A66),
    surfaceDim = Color(0xFF0F1414),
    surfaceBright = Color(0xFF353A3A),
    surfaceContainerLowest = Color(0xFF0A0F0F),
    surfaceContainerLow = Color(0xFF171D1C),
    surfaceContainer = Color(0xFF1B2120),
    surfaceContainerHigh = Color(0xFF262B2A),
    surfaceContainerHighest = Color(0xFF303635),
)

@Composable
fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) = MaterialTheme(
    colorScheme = isDarkTheme.fold(darkColourScheme, lightColourScheme),
    content = content,
)
