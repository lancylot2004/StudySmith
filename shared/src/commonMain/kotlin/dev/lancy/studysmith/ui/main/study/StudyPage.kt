package dev.lancy.studysmith.ui.main.study

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.bumble.appyx.navigation.modality.NodeContext
import com.bumble.appyx.navigation.node.LeafNode
import com.composables.icons.lucide.AppWindowMac
import com.composables.icons.lucide.Bell
import com.composables.icons.lucide.Globe
import com.composables.icons.lucide.Lucide
import dev.lancy.studysmith.structures.SessionConfiguration
import dev.lancy.studysmith.structures.data.StudyGroup
import dev.lancy.studysmith.ui.shared.ColourScheme
import dev.lancy.studysmith.ui.shared.Padding
import dev.lancy.studysmith.ui.shared.Rounded
import dev.lancy.studysmith.ui.shared.Size
import dev.lancy.studysmith.ui.shared.Str
import dev.lancy.studysmith.ui.shared.Typography
import dev.lancy.studysmith.ui.shared.dump
import dev.lancy.studysmith.utilities.fold
import dev.lancy.studysmith.utilities.oxfordJoin
import studysmith.shared.generated.resources.study_app_blocker_description
import studysmith.shared.generated.resources.study_app_blocker_title
import studysmith.shared.generated.resources.study_dnd_description
import studysmith.shared.generated.resources.study_dnd_title
import studysmith.shared.generated.resources.study_i_want_to_have
import studysmith.shared.generated.resources.study_im_studying_with
import studysmith.shared.generated.resources.study_web_blocker_description
import studysmith.shared.generated.resources.study_web_blocker_title

class StudyPage(
    nodeContext: NodeContext,
) : LeafNode(nodeContext) {
    @Composable
    override fun Content(modifier: Modifier) {
        var config by remember { mutableStateOf(SessionConfiguration()) }

        Column(
            verticalArrangement = Arrangement.spacedBy(Padding.Medium),
            horizontalAlignment = Alignment.Start,
            modifier = modifier
                .fillMaxSize()
                .padding(Padding.Large),
        ) {
            SectionTitle(Str.study_im_studying_with.dump())
            var dropdownWithExpanded by remember { mutableStateOf(false) }
            var dropdownSelectedIndex by remember { mutableStateOf(0) }

            // TODO: Tie into data.
            val GROUPS = listOf<StudyGroup>(
                StudyGroup("1", "Funky Bots", listOf("Kai", "Lawan", "Mo"), Lucide.Bell),
                StudyGroup("2", "Stardew Valley", listOf("Sani", "Alex"), Lucide.Globe),
                StudyGroup("3", "Cultivate Cultivate", listOf("Jack"), Lucide.AppWindowMac),
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(Rounded.Small)
                    .background(ColourScheme.surfaceVariant.copy(alpha = 0.5f)),
            ) {
                if (GROUPS.isEmpty()) {
                    // TODO: determine connectivity
                    item { CannotSelectGroups(true) }
                    return@LazyColumn
                }

                itemsIndexed(GROUPS) { index, item ->
                    AnimatedVisibility(dropdownWithExpanded || index == dropdownSelectedIndex) {
                        GroupItem(item) {
                            if (!dropdownWithExpanded) {
                                dropdownWithExpanded = true
                            } else {
                                dropdownSelectedIndex = index
                                dropdownWithExpanded = false
                            }
                        }
                    }
                }
            }

            SectionTitle(Str.study_i_want_to_have.dump())

            BinarySetting(
                checked = config.useAppBlocker,
                icon = Lucide.AppWindowMac,
                title = Str.study_app_blocker_title.dump(),
                description = Str.study_app_blocker_description.dump(),
            ) { config = config.copy(useAppBlocker = it) }

            BinarySetting(
                checked = config.useWebBlocker,
                icon = Lucide.Globe,
                title = Str.study_web_blocker_title.dump(),
                description = Str.study_web_blocker_description.dump(),
            ) { config = config.copy(useWebBlocker = it) }

            BinarySetting(
                checked = config.useDoNotDisturb,
                icon = Lucide.Bell,
                title = Str.study_dnd_title.dump(),
                description = Str.study_dnd_description.dump(),
            ) { config = config.copy(useDoNotDisturb = it) }
        }
    }

    @Composable
    private fun SectionTitle(string: String) = Text(
        string,
        style = Typography.headlineMedium,
        fontWeight = FontWeight.Bold,
    )

    @Composable
    private fun BinarySetting(
        checked: Boolean,
        icon: ImageVector,
        title: String,
        description: String,
        onCheckedChange: (Boolean) -> Unit,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Padding.Medium),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .size(Size.ExtraLarge)
                    .clip(Rounded.Small)
                    .background(ColourScheme.surfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(Size.Medium),
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(Padding.Small),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f).padding(Padding.Small),
            ) {
                Text(
                    title,
                    style = Typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    description,
                    style = Typography.bodyMedium,
                    color = ColourScheme.onSurfaceVariant,
                    minLines = 2,
                )
            }

            Switch(
                checked = checked,
                onCheckedChange = { onCheckedChange(it) },
            )
        }
    }

    @Composable
    private fun GroupItem(
        group: StudyGroup,
        callback: () -> Unit,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Padding.Medium),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(Size.ExtraLarge)
                .clickable { callback() },
        ) {
            Box(
                modifier = Modifier
                    .size(Size.ExtraLarge)
                    .clip(Rounded.Small)
                    .background(ColourScheme.surfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = group.thumbnail,
                    contentDescription = Str.study_im_studying_with.dump(),
                    modifier = Modifier.size(Size.Medium),
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(Padding.Small),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = group.name,
                    style = Typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = group.members.oxfordJoin(", ", placeholder = "No one is online right now.", suffix = " are online..."),
                    style = Typography.bodyMedium,
                    color = ColourScheme.onSurfaceVariant,
                )
            }
        }
    }

    @Composable
    private fun CannotSelectGroups(offline: Boolean = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(Size.ExtraLarge)
                .clickable {
                    // TODO: Navigate to "Buddies"
                },
            verticalArrangement = Arrangement.spacedBy(Padding.Small, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                offline.fold("You're offline!", "Click here to get in a group."),
                style = Typography.bodyLarge,
                color = ColourScheme.onBackground,
                fontWeight = FontWeight.Bold,
            )

            Text(
                offline.fold("Time to touch grass...?", "It's more fun with buddies!"),
                style = Typography.bodyMedium,
                color = ColourScheme.onSurfaceVariant,
            )
        }
    }
}
