package com.android.catchdesign.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    object List : Screen("list")
    object Detail : Screen("detail/{title}/{content}") {
        fun createRoute(title: String, content: String): String {
            return "detail/$title/$content"
        }
    }
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.List.route,
        modifier = modifier
    ) {
        composable(Screen.List.route) {
            ListScreen(
                modifier = Modifier,
                onItemClick = { title, content ->
                    navController.navigate(Screen.Detail.createRoute(title,content))
                }
            )
        }
        
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("content") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val content = backStackEntry.arguments?.getString("content") ?: ""
            
            DetailScreen(
                title = title,
                content = content,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
