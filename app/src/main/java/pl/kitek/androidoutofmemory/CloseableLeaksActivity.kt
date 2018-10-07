package pl.kitek.androidoutofmemory

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_closeable.*

class CloseableLeaksActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_closeable)

        fileStreamLeakBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fileStreamLeakBtn -> closeableFileLeak()
        }
    }

    private fun closeableFileLeak() {
// LEAK
        val stream = openFileOutput("leakedFile.txt", Context.MODE_PRIVATE)
        stream.write("This is just a test".toByteArray())

// FIX #1 - call .close() in finally
//        try {
//            stream.write("This is just a test".toByteArray())
//        } catch (e: Exception) {
//        } finally {
//            stream.close()
//        }

// FIX #2 - call .use { ... } it will automatically close your stream
//        stream.use {
//            it.write("This is just a test".toByteArray())
//        }

        Toast.makeText(this, R.string.kaboom, Toast.LENGTH_LONG).show()
    }
}
