package dev.lancy.studysmith.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.BackStackModel
import com.bumble.appyx.components.backstack.operation.replace
import com.bumble.appyx.components.backstack.ui.fader.BackStackFader
import com.bumble.appyx.navigation.composable.AppyxNavigationContainer
import com.bumble.appyx.navigation.modality.NodeContext
import com.bumble.appyx.navigation.node.Node
import com.bumble.appyx.utils.multiplatform.Parcelable
import com.bumble.appyx.utils.multiplatform.Parcelize
import dev.lancy.studysmith.api.Client
import dev.lancy.studysmith.ui.loggedOut.LoggedOutNode
import dev.lancy.studysmith.ui.main.MainNode
import dev.lancy.studysmith.ui.shared.NavTarget
import io.github.jan.supabase.auth.status.SessionStatus

class RootNode(
    nodeContext: NodeContext,
    private val backStack: BackStack<RootNav> = BackStack(
        model = BackStackModel(
            // TODO: Update after authentication is finalised.
            initialTargets = listOf(RootNav.LoggedOut),
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
        RootNav.LoggedOut -> LoggedOutNode(nodeContext)
    }

    @Composable
    override fun Content(modifier: Modifier) {
        AppyxNavigationContainer(
            appyxComponent = backStack,
            modifier = modifier,
        )

        LaunchedEffect(Unit) {
            Client.auth.sessionStatus.collect {
                when (it) {
                    is SessionStatus.Authenticated -> {
                        println("Session is authenticated! User: ${it.session}")
                        backStack.replace(RootNav.Main)
                    }
                    SessionStatus.Initializing -> println("Session is initializing...")
                    is SessionStatus.NotAuthenticated -> {
                        println("Session is not authenticated! ${it.isSignOut}")
                        backStack.replace(RootNav.LoggedOut)
                    }
                    is SessionStatus.RefreshFailure -> println("Refresh failure! ${it.cause}")
                }
            }
        }
    }
}
