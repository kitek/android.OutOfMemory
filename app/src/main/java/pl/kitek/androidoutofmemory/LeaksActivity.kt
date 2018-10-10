package pl.kitek.androidoutofmemory

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private val myAdapter = MyRecyclerAdapter(listOf("One", "Two", "Three", "Four"))

    class MyViewModel : ViewModel() {
        var counter: Int = 0
        var myAdapter: MyRecyclerAdapter? = null
        var myView: View? = null
    }

    private fun saveInMyViewModel() {
        val model = ViewModelProviders.of(this).get(MyViewModel::class.java)

        model.counter += 1
        model.myAdapter = myAdapter
        model.myView = someBtn
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaks)

        staticActivityBtn.setOnClickListener(this)
        staticContextBtn.setOnClickListener(this)
        staticViewBtn.setOnClickListener(this)
        staticInflaterBtn.setOnClickListener(this)
        someBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.staticActivityBtn -> staticActivityLeak()
            R.id.staticContextBtn -> staticContextLeak()
            R.id.staticViewBtn -> staticViewLeak()
            R.id.staticInflaterBtn -> staticLayoutInflaterLeak()
            R.id.singletonBtn -> singletonLeak()
            R.id.someBtn -> viewModelLeak()
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

    private fun viewModelLeak() {
        myRecyclerView.setHasFixedSize(true)
        myRecyclerView.layoutManager = LinearLayoutManager(this)
        myRecyclerView.adapter = myAdapter

        saveInMyViewModel()
    }

    class MyRecyclerAdapter(private val items: List<String>) : RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val itemView = layoutInflater.inflate(R.layout.item, parent, false)
            val labelTxt: TextView = itemView.findViewById(R.id.someTextLabel)

            return MyViewHolder(labelTxt, itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int = items.size

        class MyViewHolder(
                private val labelTxt: TextView,
                itemView: View
        ) : RecyclerView.ViewHolder(itemView) {

            fun bind(item: String) {
                labelTxt.text = item
            }
        }
    }
}
