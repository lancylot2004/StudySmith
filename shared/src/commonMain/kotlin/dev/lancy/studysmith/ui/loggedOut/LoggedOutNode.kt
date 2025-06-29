package dev.lancy.studysmith.ui.loggedOut

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
import dev.lancy.studysmith.ui.shared.NavTarget

class LoggedOutNode(
    nodeContext: NodeContext,
    private val backStack: BackStack<LoggedOutNav> = BackStack(
        model = BackStackModel(
            // TODO: Update after authentication is finalised.
            initialTargets = listOf(LoggedOutNav.Landing),
            savedStateMap = nodeContext.savedStateMap,
        ),
        visualisation = { BackStackFader(it) },
    ),
) : Node<LoggedOutNode.LoggedOutNav>(backStack, nodeContext) {
    @Parcelize
    sealed class LoggedOutNav : Parcelable, NavTarget {
        /**
         * [Landing] contains links to the authentication or onboarding flows.
         */
        data object Landing : LoggedOutNav()

        /**
         * [Login] contains the login flow for existing users. Note! This page simply presents
         * options to authenticate the user specified in the [Landing] page, by their email. This
         * might be password, OTP, etc.
         */
        data object Login : LoggedOutNav()

        /**
         * [Onboarding] contains the registration and onboarding flow for new users. Flows are
         * directed here based on whether the signed in session is new or existing. It is possible
         * to return to [Landing] if the user wishes to abort.
         */
        data object Onboarding : LoggedOutNav()
    }

    override fun buildChildNode(
        navTarget: LoggedOutNav,
        nodeContext: NodeContext,
    ): Node<*> = when (navTarget) {
        LoggedOutNav.Landing -> LandingPage(nodeContext)
        LoggedOutNav.Login -> LoginPage(nodeContext)
        LoggedOutNav.Onboarding -> TODO()
    }

    @Composable
    override fun Content(modifier: Modifier) {
        AppyxNavigationContainer(backStack, modifier)
    }
}
