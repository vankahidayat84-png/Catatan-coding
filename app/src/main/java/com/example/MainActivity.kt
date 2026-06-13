package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.ui.navigation.*
import com.example.ui.screens.*
import com.example.ui.theme.BgMain
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.StudyVaultViewModel
import com.example.viewmodel.StudyVaultViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BgMain
                ) {
                    val application = applicationContext as StudyVaultApplication
                    val viewModel: StudyVaultViewModel = viewModel(
                        factory = StudyVaultViewModelFactory(application.repository)
                    )
                    
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = SplashRoute
                    ) {
                        composable<SplashRoute> {
                            SplashScreen(
                                onTimeout = {
                                    navController.navigate(DashboardRoute) {
                                        popUpTo(SplashRoute) { inclusive = true }
                                    }
                                }
                            )
                        }
                        
                        composable<DashboardRoute> {
                            DashboardScreen(
                                viewModel = viewModel,
                                onNavigateToFolders = { navController.navigate(FoldersRoute) },
                                onNavigateToMateris = { navController.navigate(MaterisRoute) },
                                onNavigateToScripts = { navController.navigate(ScriptsRoute) },
                                onNavigateToSearch = { navController.navigate(CariMateriRoute) },
                                onNavigateToFavorite = { navController.navigate(FavoritRoute) },
                                onNavigateToSettings = { navController.navigate(PengaturanRoute) },
                                onNavigateToMateriDetail = { id -> navController.navigate(MateriDetailRoute(id)) },
                                onNavigateToScriptDetail = { id -> navController.navigate(ScriptDetailRoute(id)) }
                            )
                        }

                        composable<FoldersRoute> {
                            FoldersScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable<MaterisRoute> {
                            MaterisScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() },
                                onNavigateToDetail = { id -> navController.navigate(MateriDetailRoute(id)) }
                            )
                        }

                        composable<ScriptsRoute> {
                            ScriptsScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() },
                                onNavigateToDetail = { id -> navController.navigate(ScriptDetailRoute(id)) }
                            )
                        }

                        composable<CariMateriRoute> {
                            SearchScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() },
                                onNavigateToMateriDetail = { id -> navController.navigate(MateriDetailRoute(id)) },
                                onNavigateToScriptDetail = { id -> navController.navigate(ScriptDetailRoute(id)) }
                            )
                        }

                        composable<FavoritRoute> {
                            FavoriteScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() },
                                onNavigateToMateriDetail = { id -> navController.navigate(MateriDetailRoute(id)) },
                                onNavigateToScriptDetail = { id -> navController.navigate(ScriptDetailRoute(id)) }
                            )
                        }

                        composable<PengaturanRoute> {
                            SettingsScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable<MateriDetailRoute> { backStackEntry ->
                            val materiDetail = backStackEntry.toRoute<MateriDetailRoute>()
                            MateriDetailScreen(
                                materiId = materiDetail.materiId,
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable<ScriptDetailRoute> { backStackEntry ->
                            val scriptDetail = backStackEntry.toRoute<ScriptDetailRoute>()
                            ScriptDetailScreen(
                                scriptId = scriptDetail.scriptId,
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
