package dev.lancy.studysmith.ui.main

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bumble.appyx.components.spotlight.Spotlight
import com.bumble.appyx.components.spotlight.SpotlightModel
import com.bumble.appyx.components.spotlight.ui.fader.SpotlightFader
import com.bumble.appyx.components.spotlight.ui.slider.SpotlightSlider
import com.bumble.appyx.navigation.composable.AppyxNavigationContainer
import com.bumble.appyx.navigation.modality.NodeContext
import com.bumble.appyx.navigation.node.Node
import com.bumble.appyx.navigation.node.node
import com.bumble.appyx.utils.multiplatform.Parcelable
import com.bumble.appyx.utils.multiplatform.Parcelize
import com.composables.icons.lucide.CircleUser
import com.composables.icons.lucide.Lucide
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeChild
import dev.lancy.studysmith.ui.main.MainNode.MainNav.BuddiesPage
import dev.lancy.studysmith.ui.main.MainNode.MainNav.MePage
import dev.lancy.studysmith.ui.main.MainNode.MainNav.PlanPage
import dev.lancy.studysmith.ui.main.MainNode.MainNav.SessionsPage
import dev.lancy.studysmith.ui.main.MainNode.MainNav.StatsPage
import dev.lancy.studysmith.ui.main.MainNode.NavHeight
import dev.lancy.studysmith.ui.shared.Const
import dev.lancy.studysmith.ui.shared.NavTarget
import dev.lancy.studysmith.ui.shared.Padding
import dev.lancy.studysmith.ui.shared.RoundedPercent
import dev.lancy.studysmith.ui.shared.RoundedShape
import dev.lancy.studysmith.utilities.ScreenSize
import dev.lancy.studysmith.utilities.animatePlacement

class MainNode(
    nodeContext: NodeContext,
    private val spotlight: Spotlight<MainNav> = Spotlight(
        model = SpotlightModel(
            items = listOf(SessionsPage, BuddiesPage, PlanPage, StatsPage, MePage),
            initialActiveIndex = 0f,
            savedStateMap = nodeContext.savedStateMap,
        ),
        visualisation = {
            SpotlightFader(
                uiContext = it,
                defaultAnimationSpec = spring(stiffness = Spring.StiffnessHigh),
            )
        },
        gestureFactory = {
            SpotlightSlider.Gestures(
                transitionBounds = it,
                orientation = Orientation.Horizontal,
            )
        },
    ),
) : Node<MainNode.MainNav>(spotlight, nodeContext) {
    @Parcelize
    sealed class MainNav : Parcelable, NavTarget {
        /**
         * Study sessions are configured, started, and managed here.
         */
        data object SessionsPage : MainNav()

        /**
         * Friends and groups are managed here.
         */
        data object BuddiesPage : MainNav()

        /**
         * Study plans are created, edited, and viewed here.
         */
        data object PlanPage : MainNav()

        /**
         * Statistics, progress tracking, and insights are displayed here.
         */
        data object StatsPage : MainNav()

        /**
         * The user's profile and settings are managed here.
         */
        data object MePage : MainNav()
    }

    override fun buildChildNode(
        navTarget: MainNav,
        nodeContext: NodeContext,
    ): Node<*> = when (navTarget) {
        SessionsPage -> node(nodeContext) { Text("Sessions Page") }
        BuddiesPage -> node(nodeContext) { Text("Buddies Page") }
        PlanPage -> node(nodeContext) { Text("Plan Page") }
        StatsPage -> node(nodeContext) { Text("Stats Page") }
        MePage -> node(nodeContext) { Text("Me Page") }
    }

    @Composable
    override fun Content(modifier: Modifier) {
        val hazeState = remember { HazeState() }

        Box(modifier) {
            AppyxNavigationContainer(
                appyxComponent = spotlight,
                modifier = Modifier.fillMaxSize(),
            )

            Navigation(
                modifier = Modifier.align(Alignment.BottomCenter),
                hazeState = hazeState,
            )
        }
    }

    private object NavHeight {
        val ExpandedTop = 45.dp

        val ExpandedBottom = 70.dp

        val Collapsed = 35.dp
    }

    /**
     * The navigation bar is a blatant imitation of the new Liquid Glass iOS design language. It has
     * two states which are animated between:
     *
     * ## Expanded
     * ```
     * ╭───────────────────────────────────────────────╮
     * │ <tracker bar> stuff goes here                 │
     * ╰───────────────────────────────────────────────╯
     * ╭────────────────────────────────────────╮ ╭────╮
     * │ <nav item> ............... <nav item>  │ │ ME │
     * ╰────────────────────────────────────────╯ ╰────╯
     * ```
     *
     * ## Collapsed (shorter in height)
     * ╭───    ─╮ ╭─────────────────────────────╮ ╭────╮
     * │ <curr> │ │ <tracker bar> stuff         │ │ ME │
     * ╰─    ───╯ ╰─────────────────────────────╯ ╰────╯
     */
    @Composable
    private fun Navigation(
        modifier: Modifier = Modifier,
        hazeState: HazeState,
    ) {
        var expanded by remember { mutableStateOf(true) }

        FlowRow(
            modifier = modifier
                .fillMaxWidth()
                .padding(Padding.Medium)
                .animateContentSize()
                .clickable { expanded = !expanded },
            horizontalArrangement = Arrangement.spacedBy(Padding.Medium),
            verticalArrangement = Arrangement.spacedBy(Padding.Medium),
            itemVerticalAlignment = Alignment.CenterVertically,
            maxLines = if (expanded) 2 else 1,
        ) {
            key("tracker") {
                if (expanded) { TrackerBar(expanded, hazeState) }
            }

            MainNav(expanded, hazeState)

            key("tracker") {
                if (!expanded) { TrackerBar(expanded, hazeState) }
            }

            MeNav(expanded, hazeState)
        }
    }

    @Composable
    private fun FlowRowScope.TrackerBar(
        expanded: Boolean,
        hazeState: HazeState,
    ) {
        val trackerBarHeight by animateDpAsState(
            if (expanded) NavHeight.ExpandedTop else NavHeight.Collapsed,
            label = "TrackerBarHeight",
        )

        val trackerBarCornerRadius by animateIntAsState(
            if (expanded) RoundedPercent.LARGE else RoundedPercent.FULL,
            label = "TrackerBarCornerRadius",
        )

        val trackerBarWidth by animateDpAsState(
            ScreenSize.width -
                (Padding.Medium * if (expanded) 2 else 4) -
                (if (expanded) 0.dp else NavHeight.Collapsed * 2),
            label = "TrackerBarWidth",
        )

        Box(
            modifier = Modifier
                // The width is controlled implicitly by other elements and max row count.
                .width(trackerBarWidth)
                .height(trackerBarHeight)
                .clip(RoundedCornerShape(trackerBarCornerRadius))
                .animatePlacement()
                .hazeChild(hazeState, style = Const.HazeStyle)
                .border(1.dp, Color.Red, RoundedCornerShape(RoundedPercent.LARGE))
                .background(Color.White),
        ) {
            Text("Tracker Bar")
        }
    }

    @Composable
    private fun FlowRowScope.MainNav(
        expanded: Boolean,
        hazeState: HazeState,
    ) {
        // When the main navigation is collapsed, it will be a single icon, nominally identical to
        // the "Me" icon, which is always present. Hence it is [NavHeight.Collapsed].
        val mainNavWidth by animateDpAsState(
            if (expanded) ScreenSize.width - (Padding.Medium * 3) - NavHeight.ExpandedBottom else NavHeight.Collapsed,
            label = "MainNavWidth",
        )

        val mainNavHeight by animateDpAsState(
            if (expanded) NavHeight.ExpandedBottom else NavHeight.Collapsed,
            label = "MainNavHeight",
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .width(mainNavWidth)
                    .height(mainNavHeight)
                    .clip(RoundedShape.Full)
                    .animatePlacement()
                    .hazeChild(hazeState, style = Const.HazeStyle)
                    .border(1.dp, Color.Magenta, RoundedShape.Full)
                    .background(Color.White),
        ) {
            Text("Main Navigation Bar")
        }
    }

    @Composable
    private fun FlowRowScope.MeNav(
        expanded: Boolean,
        hazeState: HazeState,
    ) {
        val meNavSize by animateDpAsState(
            if (expanded) NavHeight.ExpandedBottom else NavHeight.Collapsed,
            label = "MeNavSize",
        )

        IconButton(
            onClick = {},
            modifier =
                Modifier
                    .size(meNavSize)
                    .clip(RoundedShape.Full)
                    .hazeChild(hazeState, style = Const.HazeStyle)
                    .border(1.dp, Color.Green, RoundedShape.Full)
                    .background(Color.White),
        ) {
            Icon(
                imageVector = Lucide.CircleUser,
                contentDescription = "About Me",
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
