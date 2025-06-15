package dev.lancy.studysmith.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.BackStackModel
import com.bumble.appyx.components.backstack.ui.fader.BackStackFader
import com.bumble.appyx.navigation.composable.AppyxNavigationContainer
import com.bumble.appyx.navigation.modality.NodeContext
import com.bumble.appyx.navigation.node.Node
import com.bumble.appyx.utils.multiplatform.Parcelable
import com.bumble.appyx.utils.multiplatform.Parcelize
import dev.lancy.studysmith.ui.main.MainNode
import dev.lancy.studysmith.ui.shared.NavTarget

class RootNode(
    nodeContext: NodeContext,
    private val backStack: BackStack<RootNav> = BackStack(
        model = BackStackModel(
            // TODO: Update after authentication is finalised.
            initialTargets = listOf(RootNav.Main),
            savedStateMap = nodeContext.savedStateMap,
        ),
        visualisation = { BackStackFader(it) },
    ),
) : Node<RootNode.RootNav>(backStack, nodeContext) {
    @Parcelize
    sealed class RootNav : Parcelable, NavTarget {
        /**
         * [Main] contains the primary pages / tabs of the application.
         */
        data object Main : RootNav()

        /**
         * [LoggedOut] contains the authentication flows and onboarding.
         */
        data object LoggedOut : RootNav()
    }

    override fun buildChildNode(
        navTarget: RootNav,
        nodeContext: NodeContext,
    ): Node<*> = when (navTarget) {
        RootNav.Main -> MainNode(nodeContext)
        RootNav.LoggedOut -> TODO()
    }

    @Composable
    override fun Content(modifier: Modifier) {
        AppyxNavigationContainer(
            appyxComponent = backStack,
            modifier = modifier,
        )
    }
}
