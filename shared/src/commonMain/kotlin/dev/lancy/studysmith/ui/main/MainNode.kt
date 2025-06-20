package dev.lancy.studysmith.ui.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.bumble.appyx.components.spotlight.Spotlight
import com.bumble.appyx.components.spotlight.SpotlightModel
import com.bumble.appyx.components.spotlight.SpotlightModel.State
import com.bumble.appyx.components.spotlight.operation.activate
import com.bumble.appyx.components.spotlight.ui.slider.SpotlightSlider
import com.bumble.appyx.interactions.model.Element
import com.bumble.appyx.interactions.ui.context.UiContext
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
import dev.lancy.studysmith.ui.main.me.MePage
import dev.lancy.studysmith.ui.shared.Animation
import dev.lancy.studysmith.ui.shared.ColourScheme
import dev.lancy.studysmith.ui.shared.Haze
import dev.lancy.studysmith.ui.shared.NavTarget
import dev.lancy.studysmith.ui.shared.Padding
import dev.lancy.studysmith.ui.shared.Rounded
import dev.lancy.studysmith.ui.shared.Size
import dev.lancy.studysmith.utilities.ScreenSize
import dev.lancy.studysmith.utilities.animatePlacement
import dev.lancy.studysmith.utilities.selectedIndex
import dev.lancy.studysmith.utilities.textHeight
import dev.lancy.studysmith.utilities.textWidth
import kotlinx.coroutines.runBlocking

class MainNode(
    nodeContext: NodeContext,
    private val visualisation: (UiContext) -> SpotlightSlider<MainNav> = { context ->
        SpotlightSlider(
            uiContext = context,
            initialState = State(
                MainNav.entries.map { State.Position(mapOf(Element(it) to State.ElementState.STANDARD)) },
                activeIndex = MainNav.entries.indexOf(MainNav.StudyPage).toFloat(),
            ),
        )
    },
    private val spotlight: Spotlight<MainNav> = Spotlight(
        model = SpotlightModel(
            items = MainNav.entries,
            initialActiveIndex = MainNav.entries.indexOf(MainNav.StudyPage).toFloat(),
            savedStateMap = nodeContext.savedStateMap,
        ),
        visualisation = visualisation,
        gestureFactory = { SpotlightSlider.Gestures(it) },
    ),
) : Node<MainNode.MainNav>(spotlight, nodeContext) {
    /**
     * Navigation targets for the main pages of the application.
     *
     * @param title The title of the navigation item.
     * @param icon Given whether the item is selected, returns the appropriate icon. This function
     * may suspend, in which case a glimmer effect will be applied while the icon is loading.
     */
    @Parcelize
    sealed class MainNav(
        val title: String,
        val icon: suspend (Boolean) -> ImageVector,
    ) : Parcelable, NavTarget {
        /**
         * Study sessions are configured, started, and managed here.
         */
        data object StudyPage : MainNav("Study", { Lucide.Play })

        /**
         * Friends and groups are managed here.
         */
        data object BuddiesPage : MainNav("Buddies", { Lucide.Users })

        /**
         * Study plans are created, edited, and viewed here.
         */
        data object PlanPage : MainNav("Plan", { Lucide.ClipboardList })

        /**
         * Statistics, progress tracking, and insights are displayed here.
         */
        data object StatsPage : MainNav("Stats", { Lucide.ChartNoAxesColumn })

        /**
         * The user's profile and settings are managed here.
         */
        data object MePage : MainNav("Me", { Lucide.CircleUser })

        companion object {
            val entries: List<MainNav> = listOf(StudyPage, BuddiesPage, PlanPage, StatsPage, MePage)
        }
    }

    override fun buildChildNode(
        navTarget: MainNav,
        nodeContext: NodeContext,
    ): Node<*> = when (navTarget) {
        MainNav.MePage -> MePage(nodeContext)
        else -> node(nodeContext) {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(runBlocking { navTarget.icon(true) }, contentDescription = null)
                Text(navTarget.title)
            }
        }
    }

    @Composable
    override fun Content(modifier: Modifier) {
        val hazeState = remember { HazeState() }
        var navExpanded by remember { mutableStateOf(true) }

        Box(modifier.fillMaxSize()) {
            AppyxNavigationContainer(
                appyxComponent = spotlight,
                modifier = Modifier
                    .fillMaxSize()
                    .haze(hazeState)
                    .background(ColourScheme.background),
            )

            TrackerBar(navExpanded, hazeState) { navExpanded = !navExpanded }
            MainNav(navExpanded, hazeState)
        }
    }

    private object NavSize {
        val TopHeight = 50.dp

        val BottomHeight = 70.dp

        val CollapsedSize = 45.dp
    }

    @Composable
    private fun BoxScope.TrackerBar(
        expanded: Boolean,
        hazeState: HazeState,
        callback: () -> Unit,
    ) {
        val paddingStart by animateDpAsState(
            if (expanded) Padding.Medium else (Padding.Medium * 2) + NavSize.CollapsedSize,
            animationSpec = Animation.medium(),
            label = "TrackerBarPaddingLeft",
        )

        val paddingBottom by animateDpAsState(
            // Expanded: Above and below main nav, and main nav itself.
            if (expanded) Padding.Medium * 2 + NavSize.BottomHeight else Padding.Medium,
            animationSpec = Animation.medium(),
            label = "TrackerBarPaddingBottom",
        )

        val height by animateDpAsState(
            if (expanded) NavSize.TopHeight else NavSize.CollapsedSize,
            animationSpec = Animation.short(),
            label = "TrackerBarHeight",
        )

        Box(
            modifier = Modifier
                // Align to bottom, then control positioning with padding.
                .align(Alignment.BottomEnd)
                .padding(start = paddingStart, end = Padding.Medium, bottom = paddingBottom)
                // The width is controlled implicitly by other elements and max row count.
                .fillMaxWidth()
                .height(height)
                .clip(Rounded.Large)
                .animatePlacement()
                .hazeChild(hazeState, shape = Rounded.Large, style = Haze.Primary)
                .clickable { callback() },
        ) {
            Text("Tracker Bar")
        }
    }

    @Composable
    private fun BoxScope.IconTextNavItem(
        item: MainNav,
        expanded: Boolean,
        interactionSource: MutableInteractionSource,
        index: Int = MainNav.entries.indexOf(item),
    ) {
        val selected = isSelected(item)

        // If not expanded, only show if selected. (Stop here.)
        if (!expanded && !selected) return

        val colour by animateColorAsState(
            if (selected) ColourScheme.secondary else ColourScheme.onBackground,
            animationSpec = Animation.medium(),
            label = "NavItem[${item.title}]Colour",
        )

        val height by animateDpAsState(
            if (expanded) NavSize.BottomHeight else NavSize.CollapsedSize,
            animationSpec = Animation.medium(),
            label = "MainNavHeight",
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .padding(Padding.Small / 2)
                .clip(Rounded.Medium)
                .selectable(
                    selected = selected,
                    interactionSource = interactionSource,
                    indication = null,
                    role = Role.Tab,
                ) { spotlight.activate(index.toFloat()) },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = runBlocking { item.icon(selected) },
                contentDescription = item.title,
                modifier = Modifier
                    .padding(Padding.Small)
                    .size(Size.Medium),
                tint = colour,
            )

            // If not expanded but selected, show icon only. (Stop here.)
            if (!expanded) return@Column

            Text(
                item.title,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(
                        bottom = Padding.Small,
                        start = Padding.Small,
                        end = Padding.Small,
                    ),
                color = colour,
            )
        }
    }

    @Composable
    private fun BoxScope.MainNav(
        expanded: Boolean,
        hazeState: HazeState,
    ) {
        val interactionSource = remember { MutableInteractionSource() }

        val paddingEnd by animateDpAsState(
            if (expanded) Padding.Medium else ScreenSize.width - Padding.Medium - NavSize.CollapsedSize,
            animationSpec = Animation.long(),
            label = "MainNavPaddingEnd",
        )

        TabRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Padding.Medium, end = paddingEnd, bottom = Padding.Medium)
                .align(Alignment.BottomStart)
                .background(Color.Transparent)
                .clip(Rounded.Medium)
                .hazeChild(hazeState, shape = Rounded.Medium, style = Haze.Primary),
            containerColor = Color.Transparent,
            contentColor = ColourScheme.primary,
            selectedTabIndex = spotlight.selectedIndex(),
            indicator = { Indicator(it, expanded, interactionSource) },
            divider = {},
            tabs = {
                MainNav.entries.forEachIndexed { index, item ->
                    IconTextNavItem(item, expanded, interactionSource)
                }
            },
        )
    }

    @Composable
    private fun Indicator(
        tabPositions: List<TabPosition>,
        expanded: Boolean,
        interactionSoruce: MutableInteractionSource,
    ) {
        // No indicator while collapsed.
        if (!expanded) return
        val selectedIndex = spotlight.selectedIndex()

        // No indicator if no tabs are selected.
        if (selectedIndex >= tabPositions.size) return

        val indWidth by animateDpAsState(
            // Text width of the selected item or icon (biggest), plus some padding.
            max(
                Size.Medium,
                textWidth(
                    MainNav.entries[selectedIndex].title,
                    MaterialTheme.typography.titleSmall,
                ),
            ) + Padding.Medium * 2,
            animationSpec = Animation.short(),
            label = "MainNavIndicatorWidth",
        )

        val indHeight by animateDpAsState(
            // Text and icon height, space between, and a bit extra.
            textHeight(
                MainNav.entries[selectedIndex].title,
                MaterialTheme.typography.titleSmall,
            ) + Size.Medium + Padding.Small * 1.5f,
            animationSpec = Animation.short(),
            label = "MainNavIndicatorHeight",
        )

        val anchors = remember(tabPositions) {
            DraggableAnchors {
                tabPositions.forEachIndexed { index, tab ->
                    index at tab.left.value
                }
            }
        }

        val draggableState = remember(anchors) {
            AnchoredDraggableState(
                initialValue = selectedIndex,
                anchors = anchors,
            )
        }

        LaunchedEffect(draggableState.currentValue) {
            spotlight.activate(draggableState.currentValue.toFloat())
        }

        TabRowDefaults.PrimaryIndicator(
            modifier = Modifier
                .tabIndicatorOffset(tabPositions[selectedIndex])
                .padding(vertical = Padding.Small)
                .anchoredDraggable(
                    state = draggableState,
                    orientation = Orientation.Horizontal,
                    interactionSource = interactionSoruce,
                ),
            width = indWidth,
            height = indHeight,
            color = ColourScheme.primaryContainer.copy(alpha = 0.3f),
            shape = Rounded.Small,
        )
    }

    @Composable
    private fun isSelected(item: MainNav): Boolean =
        spotlight.selectedIndex() == MainNav.entries.indexOf(item)
}
