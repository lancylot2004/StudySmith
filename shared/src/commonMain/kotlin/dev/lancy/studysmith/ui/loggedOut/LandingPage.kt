package dev.lancy.studysmith.ui.loggedOut

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.bumble.appyx.interactions.getPlatformName
import com.bumble.appyx.navigation.modality.NodeContext
import com.bumble.appyx.navigation.node.LeafNode
import dev.lancy.studysmith.api.Client
import dev.lancy.studysmith.ui.shared.ColourScheme
import dev.lancy.studysmith.ui.shared.Padding
import dev.lancy.studysmith.ui.shared.Rounded
import dev.lancy.studysmith.ui.shared.Size
import dev.lancy.studysmith.ui.shared.Typography
import dev.lancy.studysmith.utilities.fold
import io.github.jan.supabase.auth.providers.Apple
import io.github.jan.supabase.auth.providers.Github
import io.github.jan.supabase.auth.providers.Google
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import studysmith.shared.generated.resources.Res
import studysmith.shared.generated.resources.apple
import studysmith.shared.generated.resources.github
import studysmith.shared.generated.resources.google

class LandingPage(nodeContext: NodeContext) : LeafNode(nodeContext) {
    @Composable
    override fun Content(modifier: Modifier) {
        val focusManager = LocalFocusManager.current
        val interactionSource = remember { MutableInteractionSource() }
        val scope = rememberCoroutineScope()

        var emailText by remember { mutableStateOf(TextFieldValue()) }
        var emailErred by remember { mutableStateOf(false) }

        Column(
            modifier = modifier
                .clickable(indication = null, interactionSource = interactionSource) {
                    focusManager.clearFocus()
                },
            verticalArrangement = Arrangement.spacedBy(Padding.Medium),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            KamelImage(
                resource = { asyncPainterResource(Url("https://picsum.photos/500/300")) },
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.67f),
                contentScale = ContentScale.Fit,
            )

            Text(
                text = "StudySmith",
                // Extra space for title.
                modifier = Modifier.padding(top = Padding.Large),
                style = Typography.displaySmall,
                fontWeight = FontWeight.Bold,
            )

            Text(
                // TODO: Dynamic list of splash texts.
                text = "No more procrastination...?",
                // Extra space for subtitle.
                modifier = Modifier.padding(top = Padding.Large),
                style = Typography.titleLarge,
                color = ColourScheme.onSurfaceVariant,
            )

            OutlinedTextField(
                value = emailText,
                onValueChange = { emailText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        // Extra space for subtitle.
                        top = Padding.Large,
                        start = Padding.ExtraLarge,
                        end = Padding.ExtraLarge,
                    ),
                label = { Text("Email") },
                placeholder = { Text("lancelot.liu23@imperial.ac.uk") },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                    showKeyboardOnFocus = true,
                ),
                singleLine = true,
                shape = Rounded.Medium,
            )

            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Size.ExtraLarge)
                    .padding(
                        // Compensating for weird padding on text fields.
                        top = Padding.Medium,
                        start = Padding.ExtraLarge,
                        end = Padding.ExtraLarge,
                    ),
                // Only if email and password are both valid.
                enabled = emailText.text.isNotBlank() && !emailErred,
                shape = Rounded.Medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColourScheme.primary,
                    contentColor = ColourScheme.onPrimary,
                ),
            ) { Text("Continue", style = Typography.titleMedium) }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Padding.ExtraLarge),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Padding.Medium),
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = ColourScheme.outline.copy(alpha = 0.5f),
                )

                Text(
                    text = "or",
                    style = Typography.titleMedium,
                    color = ColourScheme.onSurfaceVariant,
                )

                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = ColourScheme.outline.copy(alpha = 0.5f),
                )
            }

            if (getPlatformName() == "iOS") {
                SocialSignInButton(
                    painter = painterResource(Res.drawable.apple),
                    text = "Continue with Apple",
                    containerColor = isSystemInDarkTheme().fold(Color.White, Color.Black),
                    contentColor = isSystemInDarkTheme().fold(Color.Black, Color.White),
                ) {
                    scope.launch {
                        focusManager.clearFocus()
                        Client.auth.signInWith(Apple)
                    }
                }
            }

            SocialSignInButton(
                painter = painterResource(Res.drawable.google),
                text = "Continue with Google",
                containerColor = Color.White,
                contentColor = Color(0xFF111111),
            ) {
                // TODO: Disable buttons or something

                scope.launch {
                    focusManager.clearFocus()
                    Client.auth.signInWith(Google)
                }
            }

            SocialSignInButton(
                painter = painterResource(Res.drawable.github),
                text = "Continue with GitHub",
                containerColor = Color.Black,
                contentColor = Color.White,
            ) {
                scope.launch {
                    focusManager.clearFocus()
                    Client.auth.signInWith(Github)
                }
            }
        }
    }

    @Composable
    fun SocialSignInButton(
        painter: Painter,
        text: String,
        containerColor: Color,
        contentColor: Color,
        onClick: () -> Unit,
    ) {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(Size.ExtraLarge)
                .padding(horizontal = Padding.ExtraLarge),
            shape = Rounded.Medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor,
            ),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Padding.ExtraLarge),
            ) {
                Icon(
                    painter = painter,
                    contentDescription = text,
                    modifier = Modifier.size(Size.Medium),
                    tint = Color.Unspecified,
                )

                Spacer(Modifier.width(Padding.Medium))

                Text(
                    text,
                    style = Typography.titleMedium,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                )
            }
        }
    }
}
