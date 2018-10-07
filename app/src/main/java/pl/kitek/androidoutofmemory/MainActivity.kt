package pl.kitek.androidoutofmemory

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        closeableBtn.setOnClickListener(this)
        innerClassesBtn.setOnClickListener(this)
        moreLeaksBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.closeableBtn -> startActivity(Intent(this, CloseableLeaksActivity::class.java))
            R.id.innerClassesBtn -> startActivity(Intent(this, NonStaticInnerClassesActivity::class.java))
            R.id.moreLeaksBtn -> startActivity(Intent(this, LeaksActivity::class.java))
        }
    }
}
