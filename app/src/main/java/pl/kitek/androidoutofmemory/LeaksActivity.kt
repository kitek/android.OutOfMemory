package pl.kitek.androidoutofmemory

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_leaks.*

@SuppressLint("StaticFieldLeak", "SetTextI18n", "MissingPermission", "CheckResult")
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
    private lateinit var locationClient: FusedLocationProviderClient

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

    private val locationCallback = object : LocationCallback() {

        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            val location = locationResult.locations.firstOrNull() ?: return
            someTextLabel.text = "Lat: ${location.latitude} Lng: ${location.longitude}"
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaks)

        locationClient = LocationServices.getFusedLocationProviderClient(this)

        listOf(staticActivityBtn, staticContextBtn, staticViewBtn, staticInflaterBtn, someBtn,
                observableBtn
        ).forEach { it.setOnClickListener(this) }
    }

    private val disposables = CompositeDisposable()

    override fun onPause() {
        super.onPause()

        locationClient.removeLocationUpdates(locationCallback)
        disposables.clear()
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.staticActivityBtn -> staticActivityLeak()
            R.id.staticContextBtn -> staticContextLeak()
            R.id.staticViewBtn -> staticViewLeak()
            R.id.staticInflaterBtn -> staticLayoutInflaterLeak()
            R.id.singletonBtn -> singletonLeak()
            R.id.someBtn -> viewModelLeak()
            R.id.locationBtn -> locationLeak()
            R.id.observableBtn -> observableLeak()
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

    private fun locationLeak() {
        val permission = checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            val locationRequest = LocationRequest()
            locationRequest.interval = 1000
            locationRequest.fastestInterval = 1000
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

            locationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    private fun observableLeak() {
        val disposable = getResultsAsObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ next ->
                    someTextLabel.text = "Next: $next"
                }, { err ->
                    someTextLabel.text = "Error: $err"
                })

// Uncomment to fix observable leak
//        disposables.add(disposable)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationLeak()
    }

    private fun getResultsAsObservable(): Observable<Int> {
        return Observable.create {
            for (i in 1..10) {
                if (!it.isDisposed) {
                    it.onNext(i)
                    try {
                        Thread.sleep(5000)
                    } catch (e: Exception) {
                    }
                }
            }
            if (!it.isDisposed) it.onComplete()
        }
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
