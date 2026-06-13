package com.example.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object SplashRoute

@Serializable
object DashboardRoute

@Serializable
object FoldersRoute

@Serializable
object MaterisRoute

@Serializable
object ScriptsRoute

@Serializable
object CariMateriRoute

@Serializable
object CariScriptRoute

@Serializable
object FavoritRoute

@Serializable
object PengaturanRoute

// Detail routes
@Serializable
data class MateriDetailRoute(val materiId: Int = -1)

@Serializable
data class ScriptDetailRoute(val scriptId: Int = -1)
