package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Workspaces
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.FloatingAssistantOverlay
import com.example.ui.components.JarvisHeader
import com.example.ui.screens.CommandHubScreen
import com.example.ui.screens.LogsScreen
import com.example.ui.screens.MemoryScreen
import com.example.ui.screens.PluginsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.WorkflowsScreen
import com.example.ui.theme.JarvisCyan
import com.example.ui.theme.JarvisObsidianDark
import com.example.ui.theme.JarvisSurfaceDark
import com.example.ui.theme.JarvisTextPrimary
import com.example.ui.theme.JarvisTextSecondary

data class NavTab(
    val title: String,
    val icon: ImageVector,
    val tag: String
)

@Composable
fun JarvisMainApp(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val executionState by viewModel.executionState.collectAsState()
    val isFloatingHudVisible by viewModel.isFloatingHudVisible.collectAsState()

    val tabs = listOf(
        NavTab("Command", Icons.Default.AutoAwesome, "nav_command_hub"),
        NavTab("Workflows", Icons.Default.Workspaces, "nav_workflows"),
        NavTab("Plugins", Icons.Default.Extension, "nav_plugins"),
        NavTab("Memory", Icons.Default.Memory, "nav_memory"),
        NavTab("Logs", Icons.Default.History, "nav_logs"),
        NavTab("Settings", Icons.Default.Settings, "nav_settings")
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = JarvisObsidianDark,
        topBar = {
            JarvisHeader(
                executionState = executionState,
                isFloatingHudActive = isFloatingHudVisible,
                onToggleHud = { viewModel.toggleFloatingHud() },
                modifier = Modifier
                    .padding(WindowInsets.systemBars.asPaddingValues())
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = JarvisSurfaceDark,
                contentColor = JarvisTextPrimary,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 10.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = JarvisCyan,
                            indicatorColor = JarvisCyan,
                            unselectedIconColor = JarvisTextSecondary,
                            unselectedTextColor = JarvisTextSecondary
                        ),
                        modifier = Modifier.testTag(tab.tag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(JarvisObsidianDark)
        ) {
            when (selectedTab) {
                0 -> CommandHubScreen(viewModel = viewModel)
                1 -> WorkflowsScreen(viewModel = viewModel)
                2 -> PluginsScreen(viewModel = viewModel)
                3 -> MemoryScreen(viewModel = viewModel)
                4 -> LogsScreen(viewModel = viewModel)
                5 -> SettingsScreen(viewModel = viewModel)
            }

            // Floating Assistant Overlay Bar
            FloatingAssistantOverlay(
                isVisible = isFloatingHudVisible,
                executionState = executionState,
                onRunPrompt = { prompt -> viewModel.runPromptCommand(prompt) },
                onPause = { viewModel.pauseExecution() },
                onResume = { viewModel.resumeExecution() },
                onStop = { viewModel.stopExecution() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }
    }
}
