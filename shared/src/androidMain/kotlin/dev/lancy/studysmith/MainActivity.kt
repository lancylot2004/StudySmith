package dev.lancy.studysmith

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.bumble.appyx.navigation.integration.NodeActivity
import com.bumble.appyx.navigation.integration.NodeHost
import com.bumble.appyx.navigation.platform.AndroidLifecycle
import dev.lancy.studysmith.api.Client
import dev.lancy.studysmith.ui.RootNode
import dev.lancy.studysmith.ui.shared.AppTheme
import dev.theolm.rinku.compose.ext.Rinku
import io.github.jan.supabase.auth.handleDeeplinks

class MainActivity : NodeActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (intent.scheme == "studysmith" && intent.data?.host == "callback") {
            Client.supabase.handleDeeplinks(intent) {
                // TODO:???
            }
        }

        setContent {
            Rinku {
                AppTheme {
                    Scaffold(Modifier.safeDrawingPadding()) { _ ->
                        NodeHost(
                            lifecycle = AndroidLifecycle(LocalLifecycleOwner.current.lifecycle),
                            integrationPoint = appyxIntegrationPoint,
                        ) { RootNode(nodeContext = it) }
                    }
                }
            }
        }
    }
}
