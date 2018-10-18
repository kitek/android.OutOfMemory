package pl.kitek.androidoutofmemory

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        closeableBtn.setOnClickListener(this)
        innerClassesBtn.setOnClickListener(this)
        moreLeaksBtn.setOnClickListener(this)
        imagesBtn.setOnClickListener(this)

        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        Timber.tag("kitek").d("memoryClass: ${manager.memoryClass} ")
        Timber.tag("kitek").d("largeMemoryClass: ${manager.largeMemoryClass} ")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.closeableBtn -> startActivity(Intent(this, CloseableLeaksActivity::class.java))
            R.id.innerClassesBtn -> startActivity(Intent(this, SomeActivity::class.java))
            R.id.moreLeaksBtn -> startActivity(Intent(this, LeaksActivity::class.java))
            R.id.imagesBtn -> startActivity(Intent(this, ImageActivity::class.java))
        }
    }
}
