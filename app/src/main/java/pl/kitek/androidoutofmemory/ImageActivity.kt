package pl.kitek.androidoutofmemory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        Picasso.get().load(VERY_BIG_URL)
                .resize(256, 256) // Load resized image to reduce memory usage
                .centerCrop()
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(imageView)
        /*
        .into(imageView, object : Callback {
            override fun onSuccess() {
                val bitmap = (imageView.drawable as? BitmapDrawable?)?.bitmap
                Timber.tag("kitek").d("onSuccess: ${(bitmap?.allocationByteCount)} ")
            }

            override fun onError(e: Exception?) {
                Timber.tag("kitek").d("onError: $e ")
            }
        })*/
    }

    companion object {
        private const val VERY_BIG_URL = "https://eoimages.gsfc.nasa.gov/images/imagerecords/73000/73751/world.topo.bathy.200407.3x21600x10800.jpg"
    }

}
