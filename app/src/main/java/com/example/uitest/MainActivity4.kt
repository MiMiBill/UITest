package com.example.uitest

import android.graphics.Color
import android.graphics.Color.red
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.*

class MainActivity4 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)
        setView1()
        setView2()
        setView3()
    }

    private fun setView3() {
        val imageView = findViewById<ShapeableImageView>(R.id.img11)
        val url = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fup.enterdesk.com%2Fedpic_source%2F2f%2F6c%2F18%2F2f6c18e670129516c67a756f7a6b7199.jpg&refer=http%3A%2F%2Fup.enterdesk.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1666950122&t=f574adfe5d8b2449c3d7faedd64f15e4"
        Glide.with(this).load(url).into(imageView)
        imageView.setBackgroundColor(Color.GREEN)


    }

    private fun setView2() {
        // 代码设置 聊天框效果
        val textView = findViewById<TextView>(R.id.img10)
        val shapeAppearanceModel3 = ShapeAppearanceModel.builder().apply {
            setAllCorners(RoundedCornerTreatment())
            setAllCornerSizes(20f)
            setRightEdge(object : TriangleEdgeTreatment(20f, false) {
                // center 位置 ， interpolation 角的大小
                override fun getEdgePath(length: Float, center: Float, interpolation: Float, shapePath: ShapePath) {
                    super.getEdgePath(length, 35f, interpolation, shapePath)
                }
            })
        }.build()
        val drawable3 = MaterialShapeDrawable(shapeAppearanceModel3).apply {
            setTint(ContextCompat.getColor(this@MainActivity4, R.color.black))
            paintStyle = Paint.Style.FILL
        }
        (textView.parent as ViewGroup).clipChildren = false // 不限制子view在其范围内
        textView.setTextColor(Color.WHITE)
        textView.background = drawable3
    }

    private fun setView1(){
        // 代码设置 角和边
        val textView = findViewById<TextView>(R.id.img9)
        val shapeAppearanceModel2 = ShapeAppearanceModel.builder().apply {
            setAllCorners(RoundedCornerTreatment())
            setAllCornerSizes(50f)
            setAllEdges(TriangleEdgeTreatment(50f, false))
        }.build()
        val drawable2 = MaterialShapeDrawable(shapeAppearanceModel2).apply {
            setTint(Color.BLUE)
            paintStyle = Paint.Style.FILL_AND_STROKE
            strokeWidth = 50f
            strokeColor = ContextCompat.getColorStateList(this@MainActivity4, R.color.red)
        }
        (textView.parent as ViewGroup).clipChildren = false // 不限制子view在其范围内
        textView.setTextColor(Color.WHITE)
        textView.background = drawable2

    }
}