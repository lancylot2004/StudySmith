package dev.lancy.studysmith.utilities

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.round
import dev.lancy.studysmith.ui.shared.Animation
import kotlinx.coroutines.launch

fun Modifier.animatePlacement(): Modifier = composed {
    val scope = rememberCoroutineScope()
    var targetOffset by remember { mutableStateOf(IntOffset.Zero) }
    var animatable by remember {
        mutableStateOf<Animatable<IntOffset, AnimationVector2D>?>(null)
    }

    val intermediate = this.onPlaced {
        val position = it.positionInParent()
        val centerOffset = position - Offset(it.size.width / 2f, it.size.height / 2f)
        targetOffset = centerOffset.round()
    }

    intermediate.offset {
        // Animate to the new target offset when alignment changes.
        val anim = animatable
            ?: Animatable(targetOffset, IntOffset.VectorConverter).also { animatable = it }
        if (anim.targetValue != targetOffset) {
            scope.launch {
                anim.animateTo(targetOffset, Animation.short())
            }
        }

        // Offset the child in the opposite direction to the targetOffset, and slowly catch
        // up to zero offset via an animation to achieve an overall animated movement.
        animatable?.let { it.value - targetOffset } ?: IntOffset.Zero
    }
}

@Composable
internal expect fun getScreenSize(): DpSize

val ScreenSize: DpSize
    @Composable get() = getScreenSize()
