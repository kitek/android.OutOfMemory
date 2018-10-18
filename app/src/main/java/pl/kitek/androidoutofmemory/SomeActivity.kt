package pl.kitek.androidoutofmemory

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.lang.ref.WeakReference

@SuppressLint("StaticFieldLeak")
class SomeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var someTextLabel: TextView
    private var someAsyncTask: AsyncTask<*, *, *>? = null

    private lateinit var myThread: Thread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_leaks)
        someTextLabel = findViewById(R.id.someTextLabel)

// FIX Leak
//        myThread = Thread(MyRunnable())
//        myThread.start()
    }

// FIX Leak
//    override fun onDestroy() {
//        myThread.interrupt()
//        super.onDestroy()
//    }

    private class MyRunnable : Runnable {

        override fun run() {

            try {
                Thread.sleep(5000) // Background processing

                if (Thread.interrupted()) {
                    return
                }
            } catch (e: InterruptedException) {
            } finally {
            }
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.innerAsyncTaskBtn -> {
                someAsyncTask = BetterAsyncTask(WeakReference(someTextLabel))
                someAsyncTask?.execute()
            }
            R.id.anonymousRunnableBtn -> {
                Thread(Runnable {
                    Thread.sleep(5000) // Background processing

                    runOnUiThread { someTextLabel.setText(R.string.another_text) }
                }).start()
            }
        }
    }

    private inner class AnotherAsyncTask : AsyncTask<Void, Void, Void?>() {

        override fun doInBackground(vararg params: Void?): Void? {
            Thread.sleep(5000) // Background processing

            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)

            someTextLabel.text = getString(R.string.another_text)
        }
    }

    private class BetterAsyncTask(
            val labelRef: WeakReference<TextView>
    ) : AsyncTask<Void, Void, Void?>() {

        override fun doInBackground(vararg params: Void?): Void? {
            Thread.sleep(5000) // Background processing
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            labelRef.get()?.setText(R.string.another_text)
        }
    }

    override fun onPause() {
        super.onPause()
        someAsyncTask?.cancel(true)
    }
}
