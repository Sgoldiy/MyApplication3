package com.smarttv.myapplication

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddEditNote : Screen("add_edit_note")
    object Settings : Screen("settings")
}
