package pl.kitek.androidoutofmemory

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_nonstaticinnerclasses.*

class NonStaticInnerClassesActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nonstaticinnerclasses)

        innerAsyncTaskBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.innerAsyncTaskBtn -> MyAsyncTask().execute()
        }
    }

    private inner class MyAsyncTask : AsyncTask<Void, Void, Int>() {

        override fun doInBackground(vararg param: Void?): Int {
            // Some background processing
            Thread.sleep(5000)

            return R.color.colorPrimary
        }

        override fun onPostExecute(color: Int) {
            super.onPostExecute(color)

            val backgroundColor = ContextCompat.getColor(this@NonStaticInnerClassesActivity, color)
            innerAsyncTaskBtn.setBackgroundColor(backgroundColor)
        }
    }
}
