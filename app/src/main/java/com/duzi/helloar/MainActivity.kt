package com.duzi.helloar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.FixedHeightViewSizer
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.BaseArFragment
import com.google.ar.sceneform.ux.TransformableNode
import java.util.function.Function


class MainActivity : AppCompatActivity() {

    lateinit var fragment: ArFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val memoLayout = LayoutInflater.from(this).inflate(R.layout.memo_view, null)
        val memoTitle = memoLayout.findViewById<TextView>(R.id.memoTitle)
        val memoCard = memoLayout.findViewById<ImageView>(R.id.memoCard)

        memoTitle.text = "ARCore Test!"


        fragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment
        fragment.setOnTapArPlaneListener(BaseArFragment.OnTapArPlaneListener { hitResult, plane, motionEvent ->
            val anchor : Anchor = hitResult.createAnchor()

            ViewRenderable.builder()
                .setView(this, memoLayout)
                .setSizer(FixedHeightViewSizer(0.20f))
                .build()
                .thenAccept { renderable ->
                    addModelToScene(anchor, renderable)
                }
                .exceptionally(
                    Function<Throwable, Void?> { throwable: Throwable? ->
                        Log.e(TAG, "Unable to load Renderable.", throwable)
                        null
                    }
                )
        })
    }

    private fun addModelToScene(anchor: Anchor, renderable: Renderable?) {
        val anchorNode : AnchorNode = AnchorNode(anchor)
        val transform : TransformableNode = TransformableNode(fragment.transformationSystem)
        transform.setParent(anchorNode)
        transform.renderable = renderable
        fragment.arSceneView.scene.addChild(anchorNode)
        transform.select()
    }

    companion object {
        const val TAG = "MainActivity"
    }
}