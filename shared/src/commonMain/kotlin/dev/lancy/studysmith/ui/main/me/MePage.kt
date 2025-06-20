package dev.lancy.studysmith.ui.main.me

import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.bumble.appyx.navigation.modality.NodeContext
import com.bumble.appyx.navigation.node.LeafNode
import dev.lancy.studysmith.api.Client
import io.github.jan.supabase.auth.SignOutScope
import kotlinx.coroutines.launch

class MePage(
    nodeContext: NodeContext,
) : LeafNode(nodeContext) {
    @Composable
    override fun Content(modifier: Modifier) {
        val scope = rememberCoroutineScope()
        IconButton(
            onClick = { scope.launch { Client.auth.signOut(SignOutScope.GLOBAL) } },
        ) { Text("asdf") }
    }
}
