package pl.kitek.androidoutofmemory

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_leaks.*

@SuppressLint("StaticFieldLeak")
class LeaksActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        @JvmField var inflater: LayoutInflater? = null
        @JvmField var activity: Activity? = null
        @JvmField var context: Context? = null
        private var view: View? = null
    }

    object MySingleton {
        var context: Context? = null

        fun doSomething() = context?.getString(R.string.staticActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaks)

        staticActivityBtn.setOnClickListener(this)
        staticContextBtn.setOnClickListener(this)
        staticViewBtn.setOnClickListener(this)
        staticInflaterBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.staticActivityBtn -> staticActivityLeak()
            R.id.staticContextBtn -> staticContextLeak()
            R.id.staticViewBtn -> staticViewLeak()
            R.id.staticInflaterBtn -> staticLayoutInflaterLeak()
            R.id.singletonBtn -> singletonLeak()
        }

        Toast.makeText(this, R.string.kaboom, Toast.LENGTH_SHORT).show()
    }

    private fun staticActivityLeak() {
        activity = this
    }

    private fun staticContextLeak() {
        context = this
    }

    private fun staticViewLeak() {
        view = findViewById(R.id.staticViewBtn)
    }

    private fun staticLayoutInflaterLeak() {
        inflater = LayoutInflater.from(this)
    }

    private fun singletonLeak() {
        MySingleton.context = this
        MySingleton.doSomething()
    }
}
