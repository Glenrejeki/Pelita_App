package com.example.pelitaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // TODO: nanti ganti ini dengan DataStore (SettingsDataStore) untuk theme persist
            var darkTheme by remember { mutableStateOf(false) }

            PelitaTheme(darkTheme = darkTheme) {
                PelitaAppRoot(
                    darkTheme = darkTheme,
                    onToggleTheme = { darkTheme = !darkTheme }
                )
            }
        }
    }
}

/** ROUTES (nanti pindah ke core/ui/navigation/Routes.kt) */
private object Routes {
    const val FEED = "feed"
    const val SEARCH = "search"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"
    const val CREATE_POST = "create_post"
    const val POST_DETAIL = "post_detail"
}

/** Bottom bar item model */
private data class BottomItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

@Composable
private fun PelitaAppRoot(
    darkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val navController = rememberNavController()

    val bottomItems = remember {
        listOf(
            BottomItem(Routes.FEED, "Home", Icons.Default.Home),
            BottomItem(Routes.SEARCH, "Search", Icons.Default.Search),
            BottomItem(Routes.PROFILE, "Profile", Icons.Default.Person),
            BottomItem(Routes.SETTINGS, "Settings", Icons.Default.Settings),
        )
    }

    val currentRoute = navController.currentRoute()

    Scaffold(
        topBar = {
            PelitaTopBar(
                title = "Pelita App",
                darkTheme = darkTheme,
                onToggleTheme = onToggleTheme
            )
        },
        bottomBar = {
            // Bottom bar hanya muncul di tab utama
            val showBottomBar = currentRoute in setOf(
                Routes.FEED, Routes.SEARCH, Routes.PROFILE, Routes.SETTINGS
            )
            if (showBottomBar) {
                PelitaBottomBar(
                    items = bottomItems,
                    navController = navController
                )
            }
        },
        floatingActionButton = {
            // FAB muncul di Feed (mirip sketsa kamu)
            if (currentRoute == Routes.FEED) {
                FloatingActionButton(
                    onClick = { navController.navigate(Routes.CREATE_POST) }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Create Post")
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            PelitaNavGraph(navController = navController)
        }
    }
}

/** NAV GRAPH (nanti pindah ke core/ui/navigation/NavGraph.kt) */
@Composable
private fun PelitaNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.FEED
    ) {
        composable(Routes.FEED) {
            SimpleScreen(title = "Home / Feed") {
                Text("Nanti di sini: list post + like + comment + repost")
            }
        }
        composable(Routes.SEARCH) {
            SimpleScreen(title = "Search") {
                Text("Nanti di sini: search accounts & posts")
            }
        }
        composable(Routes.PROFILE) {
            SimpleScreen(title = "Profile") {
                Text("Nanti di sini: bio + posts user + followers/following")
            }
        }
        composable(Routes.SETTINGS) {
            SimpleScreen(title = "Settings") {
                Text("Nanti di sini: edit profile, update password, logout, delete, theme")
            }
        }
        composable(Routes.CREATE_POST) {
            SimpleScreen(title = "Create Post") {
                Text("Nanti di sini: input post (firman/renungan) + tombol Post")
                Spacer(Modifier.padding(6.dp))
                Button(onClick = { navController.popBackStack() }) {
                    Text("Kembali")
                }
            }
        }
        composable(Routes.POST_DETAIL) {
            SimpleScreen(title = "Post Detail") {
                Text("Nanti di sini: post detail + comment list + add comment")
            }
        }
    }
}

/** TOP BAR (nanti pindah ke core/ui/components/PelitaTopBar.kt) */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PelitaTopBar(
    title: String,
    darkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = title, fontWeight = FontWeight.SemiBold)
        },
        actions = {
            // Toggle theme sementara (nanti pindah ke Settings Screen + DataStore)
            Text(
                text = if (darkTheme) "Dark" else "Light",
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(Modifier.padding(horizontal = 6.dp))
            Switch(
                checked = darkTheme,
                onCheckedChange = { onToggleTheme() }
            )
            Spacer(Modifier.padding(horizontal = 6.dp))
        }
    )
}

/** BOTTOM BAR (nanti pindah ke core/ui/components/PelitaBottomBar.kt) */
@Composable
private fun PelitaBottomBar(
    items: List<BottomItem>,
    navController: NavHostController
) {
    val currentRoute = navController.currentRoute()

    NavigationBar {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        // Biar tab tidak numpuk backstack
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

/** Helper: ambil route saat ini */
@Composable
private fun NavHostController.currentRoute(): String? {
    val navBackStackEntry by currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

/** Placeholder screen wrapper */
@Composable
private fun SimpleScreen(
    title: String,
    content: @Composable () -> Unit
) {
    Surface {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.padding(8.dp))
            content()
        }
    }
}

/** THEME SEDERHANA (nanti pindah ke core/ui/theme/Theme.kt) */
@Composable
private fun PelitaTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val scheme = if (darkTheme) darkColorScheme() else lightColorScheme()

    MaterialTheme(
        colorScheme = scheme,
        typography = Typography(),
        content = content
    )
}
