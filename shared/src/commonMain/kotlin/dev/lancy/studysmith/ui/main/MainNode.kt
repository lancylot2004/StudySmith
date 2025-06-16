package dev.lancy.studysmith.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.bumble.appyx.components.spotlight.Spotlight
import com.bumble.appyx.components.spotlight.SpotlightModel
import com.bumble.appyx.components.spotlight.operation.activate
import com.bumble.appyx.components.spotlight.ui.fader.SpotlightFader
import com.bumble.appyx.components.spotlight.ui.slider.SpotlightSlider
import com.bumble.appyx.navigation.composable.AppyxNavigationContainer
import com.bumble.appyx.navigation.modality.NodeContext
import com.bumble.appyx.navigation.node.Node
import com.bumble.appyx.navigation.node.node
import com.bumble.appyx.utils.multiplatform.Parcelable
import com.bumble.appyx.utils.multiplatform.Parcelize
import com.composables.icons.lucide.ChartNoAxesColumn
import com.composables.icons.lucide.CircleUser
import com.composables.icons.lucide.ClipboardList
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Play
import com.composables.icons.lucide.Users
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.lancy.studysmith.ui.main.MainNode.MainNav.BuddiesPage
import dev.lancy.studysmith.ui.main.MainNode.MainNav.MePage
import dev.lancy.studysmith.ui.main.MainNode.MainNav.PlanPage
import dev.lancy.studysmith.ui.main.MainNode.MainNav.SessionsPage
import dev.lancy.studysmith.ui.main.MainNode.MainNav.StatsPage
import dev.lancy.studysmith.ui.shared.Animation
import dev.lancy.studysmith.ui.shared.ColourScheme
import dev.lancy.studysmith.ui.shared.Const
import dev.lancy.studysmith.ui.shared.NavTarget
import dev.lancy.studysmith.ui.shared.Padding
import dev.lancy.studysmith.ui.shared.RoundedPercent
import dev.lancy.studysmith.ui.shared.Shape
import dev.lancy.studysmith.ui.shared.Size
import dev.lancy.studysmith.ui.shared.Typography
import dev.lancy.studysmith.utilities.ScreenSize
import dev.lancy.studysmith.utilities.animatePlacement
import dev.lancy.studysmith.utilities.selectedIndex

class MainNode(
    nodeContext: NodeContext,
    private val spotlight: Spotlight<MainNav> = Spotlight(
        model = SpotlightModel(
            items = MainNav.entries,
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

        companion object {
            val entries: List<MainNav> =
                listOf(SessionsPage, BuddiesPage, PlanPage, StatsPage, MePage)
        }
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
                modifier = Modifier
                    .fillMaxSize()
                    .haze(hazeState)
                    .background(ColourScheme.primaryContainer),
            )

            Navigation(hazeState)
        }
    }

    private object NavHeight {
        val ExpandedTop = 50.dp

        val ExpandedBottom = 70.dp

        val Collapsed = 45.dp
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
    private fun BoxScope.Navigation(hazeState: HazeState) {
        var expanded by remember { mutableStateOf(true) }

        TrackerBar(expanded, hazeState) { expanded = !expanded }
        MainNav(expanded, hazeState)
        MeNav(expanded, hazeState)
    }

    @Composable
    private fun BoxScope.TrackerBar(
        expanded: Boolean,
        hazeState: HazeState,
        callback: () -> Unit,
    ) {
        val trackerBarHeight by animateDpAsState(
            if (expanded) NavHeight.ExpandedTop else NavHeight.Collapsed,
            animationSpec = Animation.medium(),
            label = "TrackerBarHeight",
        )

        val trackerBarCornerRadius by animateIntAsState(
            if (expanded) RoundedPercent.LARGE else RoundedPercent.FULL,
            animationSpec = Animation.medium(),
            label = "TrackerBarCornerRadius",
        )

        val trackerBarWidth by animateDpAsState(
            ScreenSize.width -
                (Padding.Medium * if (expanded) 2 else 4) -
                (if (expanded) 0.dp else NavHeight.Collapsed * 2),
            animationSpec = Animation.medium(),
            label = "TrackerBarWidth",
        )

        val trackerBarPadding by animateDpAsState(
            // Expanded: Above and below main nav, and main nav itself.
            if (expanded) Padding.Medium * 2 + NavHeight.ExpandedBottom else Padding.Medium,
            animationSpec = Animation.short(),
            label = "TrackerBarPadding",
        )

        Box(
            modifier = Modifier
                // Align to bottom, then control positioning with padding.
                .align(Alignment.BottomCenter)
                .padding(start = Padding.Medium, end = Padding.Medium, bottom = trackerBarPadding)
                // The width is controlled implicitly by other elements and max row count.
                .width(trackerBarWidth)
                .height(trackerBarHeight)
                .clip(RoundedCornerShape(trackerBarCornerRadius))
                .animatePlacement()
                .hazeChild(
                    state = hazeState,
                    style = Const.HazeStyle,
                    shape = RoundedCornerShape(trackerBarCornerRadius),
                ).clickable { callback() },
        ) {
            Text("Tracker Bar")
        }
    }

    @Composable
    private fun RowScope.IconTextNavItem(
        title: String,
        icon: ImageVector,
        selected: Boolean,
        expanded: Boolean,
        callback: () -> Unit,
    ) {
        val colour by animateColorAsState(
            if (selected) ColourScheme.secondary else ColourScheme.onBackground,
            animationSpec = Animation.medium(),
            label = "IconTextNavItemColour",
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .clickable { callback() },
            verticalArrangement = Arrangement.spacedBy(Padding.Small),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(Size.Small),
                tint = colour,
            )

            AnimatedVisibility(expanded) {
                Text(
                    text = title,
                    color = colour,
                    style = Typography.titleSmall,
                )
            }
        }
    }

    @Composable
    private fun isSelected(item: MainNav): Boolean =
        spotlight.selectedIndex() == MainNav.entries.indexOf(item)

    @Composable
    private fun BoxScope.MainNav(
        expanded: Boolean,
        hazeState: HazeState,
    ) {
        // When the main navigation is collapsed, it will be a single icon, nominally identical to
        // the "Me" icon, which is always present. Hence it is [NavHeight.Collapsed].
        val mainNavWidth by animateDpAsState(
            if (expanded) {
                // Either side, between the main and me navs, and the me nav itself.
                ScreenSize.width - (Padding.Medium * 3) - NavHeight.ExpandedBottom
            } else {
                NavHeight.Collapsed
            },
            animationSpec = Animation.medium(),
            label = "MainNavWidth",
        )

        val mainNavHeight by animateDpAsState(
            if (expanded) NavHeight.ExpandedBottom else NavHeight.Collapsed,
            animationSpec = Animation.medium(),
            label = "MainNavHeight",
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    // Align to bottom left always - no need to control positioning otherwise.
                    .align(Alignment.BottomStart)
                    .padding(start = Padding.Medium, bottom = Padding.Medium)
                    .width(mainNavWidth)
                    .height(mainNavHeight)
                    .clip(Shape.Full)
                    .animatePlacement()
                    .hazeChild(state = hazeState, style = Const.HazeStyle, shape = Shape.Full),
        ) {
            // Show also when [MePage] is selected, so that the left nav is never empty.
            AnimatedVisibility(expanded || isSelected(SessionsPage) || isSelected(MePage)) {
                IconTextNavItem(
                    title = "Study",
                    // TODO: dynamic based on if session is active or not.
                    icon = Lucide.Play,
                    selected = isSelected(SessionsPage),
                    expanded = expanded,
                ) { spotlight.activate(MainNav.entries.indexOf(SessionsPage).toFloat()) }
            }

            listOf(
                Triple("Buddies", Lucide.Users, BuddiesPage),
                Triple("Plan", Lucide.ClipboardList, PlanPage),
                Triple("Stats", Lucide.ChartNoAxesColumn, StatsPage),
            ).forEachIndexed { index, (title, icon, navItem) ->
                AnimatedVisibility(expanded || isSelected(navItem)) {
                    IconTextNavItem(
                        title = title,
                        icon = icon,
                        selected = isSelected(navItem),
                        expanded = expanded,
                    ) { spotlight.activate(MainNav.entries.indexOf(navItem).toFloat()) }
                }
            }
        }
    }

    @Composable
    private fun BoxScope.MeNav(
        expanded: Boolean,
        hazeState: HazeState,
    ) {
        val meNavSize by animateDpAsState(
            if (expanded) NavHeight.ExpandedBottom else NavHeight.Collapsed,
            animationSpec = Animation.medium(),
            label = "MeNavSize",
        )

        val meIconSize by animateDpAsState(
            if (expanded) Size.Medium else Size.Small,
            animationSpec = Animation.medium(),
            label = "MeIconSize",
        )

        val meNavColour by animateColorAsState(
            if (isSelected(MePage)) ColourScheme.secondary else ColourScheme.onBackground,
            animationSpec = Animation.short(),
            label = "MeNavColor",
        )

        IconButton(
            onClick = { spotlight.activate(MainNav.entries.indexOf(MePage).toFloat()) },
            modifier =
                Modifier
                    // Align to bottom right always - no need to control positioning otherwise.
                    .align(Alignment.BottomEnd)
                    .padding(Padding.Medium)
                    .size(meNavSize)
                    .clip(Shape.Full)
                    .hazeChild(state = hazeState, style = Const.HazeStyle, shape = Shape.Full),
        ) {
            Icon(
                imageVector = Lucide.CircleUser,
                contentDescription = "About Me",
                modifier = Modifier.size(meIconSize),
                tint = meNavColour,
            )
        }
    }
}
