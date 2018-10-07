package pl.kitek.androidoutofmemory

import android.app.Application
import android.os.StrictMode
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            val vmPolicy = StrictMode.VmPolicy.Builder()
                    .detectAll()    // .detectActivityLeaks() .detectLeakedClosableObjects()
                    .penaltyLog()   // .penaltyDeath()
                    .build()
            StrictMode.setVmPolicy(vmPolicy)

            Timber.plant(Timber.DebugTree())
        }

        super.onCreate()
    }

    companion object {
        const val FLAG = 1
    }
}
