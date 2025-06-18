package dev.lancy.studysmith.ui.loggedOut

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.bumble.appyx.navigation.modality.NodeContext
import com.bumble.appyx.navigation.node.LeafNode
import com.composables.icons.lucide.Apple
import com.composables.icons.lucide.Github
import com.composables.icons.lucide.Goal
import com.composables.icons.lucide.Lucide
import dev.lancy.studysmith.ui.shared.ColourScheme
import dev.lancy.studysmith.ui.shared.Padding
import dev.lancy.studysmith.ui.shared.Rounded
import dev.lancy.studysmith.ui.shared.Size
import dev.lancy.studysmith.ui.shared.Typography
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url

class LandingPage(nodeContext: NodeContext) : LeafNode(nodeContext) {
    @Composable
    override fun Content(modifier: Modifier) {
        val focusManager = LocalFocusManager.current
        val interactionSource = remember { MutableInteractionSource() }

        var emailText by remember { mutableStateOf(TextFieldValue()) }
        var emailErrored by remember { mutableStateOf(false) }

        var passwordText by remember { mutableStateOf(TextFieldValue()) }
        var passwordErrored by remember { mutableStateOf(false) }

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
//
//            OutlinedTextField(
//                value = passwordText,
//                onValueChange = { passwordText = it },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = Padding.ExtraLarge),
//                label = { Text("Password") },
//                visualTransformation = PasswordVisualTransformation('*'),
//                keyboardOptions = KeyboardOptions(
//                    capitalization = KeyboardCapitalization.None,
//                    autoCorrectEnabled = false,
//                    keyboardType = KeyboardType.Password,
//                    imeAction = ImeAction.Done,
//                    showKeyboardOnFocus = true,
//                ),
//                singleLine = true,
//                interactionSource = remember { MutableInteractionSource() },
//                shape = Rounded.Medium,
//            )

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
                enabled = (emailText.text.isNotBlank() && !emailErrored) &&
                    (passwordText.text.isNotBlank() && !passwordErrored),
                shape = Rounded.Medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColourScheme.primary,
                    contentColor = ColourScheme.onPrimary,
                ),
            ) {
                Text("Continue", style = Typography.titleMedium)
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Padding.ExtraLarge, vertical = Padding.Medium),
                color = ColourScheme.outline.copy(alpha = 0.5f),
            )

            OutlinedButton(
                onClick = { /* Google sign-in */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Size.ExtraLarge)
                    .padding(horizontal = Padding.ExtraLarge),
                shape = Rounded.Medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF111111),
                ),
            ) {
                Icon(
                    imageVector = Lucide.Goal,
                    contentDescription = "Google",
                    modifier = Modifier.size(Size.Medium),
                )
                Spacer(Modifier.width(Padding.Medium))
                Text("Continue with Google", style = Typography.titleMedium)
            }

            OutlinedButton(
                onClick = { /* Apple sign-in */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Size.ExtraLarge)
                    .padding(horizontal = Padding.ExtraLarge),
                shape = Rounded.Medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFA2AAAD),
                    contentColor = Color.Black,
                ),
            ) {
                Icon(
                    imageVector = Lucide.Apple,
                    contentDescription = "Apple",
                    modifier = Modifier.size(Size.Medium),
                )
                Spacer(Modifier.width(Padding.Medium))
                Text("Continue with Apple", style = Typography.titleMedium)
            }

            OutlinedButton(
                onClick = { /* GitHub sign-in */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Size.ExtraLarge)
                    .padding(horizontal = Padding.ExtraLarge),
                shape = Rounded.Medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White,
                ),
            ) {
                Icon(
                    imageVector = Lucide.Github, // Replace with actual GitHub icon
                    contentDescription = "GitHub",
                    modifier = Modifier.size(Size.Medium),
                )
                Spacer(Modifier.width(Padding.Medium))
                Text("Continue with GitHub", style = Typography.titleMedium)
            }
        }
    }
}
