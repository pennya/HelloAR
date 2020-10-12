package com.duzi.helloar

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import java.util.function.Consumer
import java.util.function.Function


class MainActivity : AppCompatActivity() {

    lateinit var fragment: ArFragment
    private var modelRenderable: ModelRenderable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment

        ModelRenderable.builder()
            .setSource(this, R.raw.beagle)
            .build()
            .thenAccept(Consumer { renderable: ModelRenderable ->
                modelRenderable = renderable
            })
            .exceptionally(
                Function<Throwable, Void?> { throwable: Throwable? ->
                    Log.e(TAG, "Unable to load Renderable.", throwable)
                    null
                }
            )
    }

    companion object {
        const val TAG = "MainActivity"
    }
}