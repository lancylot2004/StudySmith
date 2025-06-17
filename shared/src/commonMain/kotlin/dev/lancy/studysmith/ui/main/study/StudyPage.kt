package dev.lancy.studysmith.ui.main.study

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import dev.lancy.studysmith.ui.shared.ColourScheme
import dev.lancy.studysmith.ui.shared.Padding
import dev.lancy.studysmith.ui.shared.Rounded
import dev.lancy.studysmith.ui.shared.Size
import dev.lancy.studysmith.ui.shared.Str
import dev.lancy.studysmith.ui.shared.Typography
import dev.lancy.studysmith.ui.shared.dump
import studysmith.shared.generated.resources.study_app_blocker_description
import studysmith.shared.generated.resources.study_app_blocker_title
import studysmith.shared.generated.resources.study_dnd_description
import studysmith.shared.generated.resources.study_dnd_title
import studysmith.shared.generated.resources.study_i_want_to_have
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
            Text(
                Str.study_i_want_to_have.dump(),
                style = Typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )

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
                )
            }

            Switch(
                checked = checked,
                onCheckedChange = { onCheckedChange(it) },
            )
        }
    }
}
