package ru.aries.hacaton.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import org.koin.android.ext.android.inject
import ru.aries.hacaton.base.theme.ArielTheme
import ru.aries.hacaton.data.data_store.DataStorePrefs

class MainActivity : ComponentActivity() {

    private val dataStore: DataStorePrefs by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ArielTheme {
                MainNavigation()
            }
        }
    }
}

