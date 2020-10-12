package com.duzi.helloar

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.BaseArFragment
import com.google.ar.sceneform.ux.TransformableNode
import java.util.function.Consumer
import java.util.function.Function


class MainActivity : AppCompatActivity() {

    lateinit var fragment: ArFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment
        fragment.setOnTapArPlaneListener(BaseArFragment.OnTapArPlaneListener { hitResult, plane, motionEvent ->
            val anchor : Anchor = hitResult.createAnchor()

            ModelRenderable.builder()
                .setSource(this, Uri.parse("soccer.sfb"))
                .build()
                .thenAccept(Consumer { renderable: ModelRenderable ->
                    addModelToScene(anchor, renderable)
                })
                .exceptionally(
                    Function<Throwable, Void?> { throwable: Throwable? ->
                        Log.e(TAG, "Unable to load Renderable.", throwable)
                        null
                    }
                )
        })
    }

    private fun addModelToScene(anchor: Anchor, it: ModelRenderable?) {
        val anchorNode : AnchorNode = AnchorNode(anchor)
        val transform : TransformableNode = TransformableNode(fragment.transformationSystem)
        transform.setParent(anchorNode)
        transform.renderable = it
        fragment.arSceneView.scene.addChild(anchorNode)
        transform.select()
    }

    companion object {
        const val TAG = "MainActivity"
    }
}