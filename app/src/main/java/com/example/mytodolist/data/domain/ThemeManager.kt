package com.example.mytodolist.data.domain

import android.content.Context
import android.content.SharedPreferences


class ThemePreferences(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    fun getThemeMode(): Theme {
        val themeName = sharedPreferences.getString("theme_mode", Theme.SYSTEM.name)
        return Theme.valueOf(themeName ?: Theme.SYSTEM.name)
    }

    fun setThemeMode(themeMode: Theme) {
        sharedPreferences.edit().putString("theme_mode", themeMode.name).apply()
    }
}