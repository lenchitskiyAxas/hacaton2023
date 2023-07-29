package ru.aries.hacaton.main

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ru.aries.hacaton.di.setApiRoute
import ru.aries.hacaton.di.setMainModule
import ru.aries.hacaton.di.setModels
import ru.aries.hacaton.di.setUseCase

class HacatonApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this@HacatonApp)
        startKoin {
            printLogger(Level.INFO)
            androidContext(this@HacatonApp)
            modules(
                setModels,
                setUseCase,
                setMainModule,
                setApiRoute
            )

        }
    }
}