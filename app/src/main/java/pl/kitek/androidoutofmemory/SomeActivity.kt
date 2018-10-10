package pl.kitek.androidoutofmemory

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_more_leaks.*

@SuppressLint("StaticFieldLeak")
class SomeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var someTextLabel: TextView
    private var someAsyncTask: AnotherAsyncTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_leaks)
        someTextLabel = findViewById(R.id.someTextLabel)

        innerAsyncTaskBtn.setOnClickListener(this)
        anonymousRunnableBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.innerAsyncTaskBtn -> {
                someAsyncTask = AnotherAsyncTask()
                someAsyncTask?.execute()
            }
            R.id.anonymousRunnableBtn -> {
                Thread(SomeRunnable(someTextLabel)).start()
            }
        }
    }

    private class SomeRunnable(private val someTextView: TextView) : Runnable {
        override fun run() {
            Thread.sleep(5000) // Background processing
            someTextView.setText(R.string.another_text)
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
}
