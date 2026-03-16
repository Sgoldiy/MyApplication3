package com.smarttv.myapplication

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun App() {
    var isDarkTheme by remember { mutableStateOf(false) }

    val colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()

    MaterialTheme(colorScheme = colorScheme) {
        val navController = rememberNavController()
        val viewModel: NotesViewModel = viewModel { NotesViewModel(InMemoryNotesRepository()) }

        NavHost(navController = navController, startDestination = Screen.Home.route) {
            composable(Screen.Home.route) {
                HomeScreen(navController, viewModel)
            }
            composable(Screen.AddEditNote.route) {
                AddEditNoteScreen(navController, viewModel)
            }
            composable(Screen.Settings.route) {
                SettingsScreen(navController, isDarkTheme) { isDarkTheme = it }
            }
        }
    }
}
