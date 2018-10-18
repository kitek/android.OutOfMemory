package pl.kitek.androidoutofmemory

import android.app.Application
import android.os.StrictMode
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(this)) return
            LeakCanary.install(this)

            val vmPolicy = StrictMode.VmPolicy.Builder()
                    .detectAll()    // .detectActivityLeaks() .detectLeakedClosableObjects()
                    .penaltyLog()   // .penaltyDeath()
                    .build()
            StrictMode.setVmPolicy(vmPolicy)

            Timber.plant(Timber.DebugTree())
        }
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        
        Timber.tag("kitek").d("onTrimMemory: $level ")

    }
}
