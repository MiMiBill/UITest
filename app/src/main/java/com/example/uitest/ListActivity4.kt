package com.example.uitest

import android.app.Activity
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.takeWhile

/**
 * 列表上下带有透明度的列表
 */
class ListActivity4 : AppCompatActivity() {

    lateinit var recyclerView:RecyclerView
    lateinit var adapter: ListAdapter;
    var list = ArrayList<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list4)
        recyclerView = findViewById(R.id.list)
        for (index in 1..30){
            list.add(index)
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
            addItemDecoration(itemDecoration)
            adapter = ListAdapter(list).also {
                ListActivity4@this.adapter = it;
                it.notifyDataSetChanged()
            }
        }
        var handle = Handler()
    }

    var mPaint = Paint()
    var linearGradient:LinearGradient? = null
    var recycleViewfirstComeInFlag:Boolean = false;

    private val itemDecoration = object :RecyclerView.ItemDecoration() {
        var layerId = 0;
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDraw(c, parent, state)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                layerId = c.saveLayer(0.0f, 0.0f, parent.getWidth().toFloat(), parent.getHeight().toFloat(), mPaint);
            }
        }

        override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDrawOver(c, parent, state)

            if (linearGradient == null){
                linearGradient = LinearGradient(0f,(parent.getHeight()-54.dp2Px()).toFloat(), 0f,
                    (parent.getHeight()).toFloat(), intArrayOf(Color.parseColor("#FFFFFFFF"),Color.parseColor("#00FFFFFF")),
                    floatArrayOf(0.2f,0.9f),
                    Shader.TileMode.CLAMP);
            }

            mPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            mPaint.setShader(linearGradient);
            c.drawRect(0f,(parent.getHeight()-54.dp2Px()).toFloat(),parent.getWidth().toFloat(),parent.getHeight().toFloat(),mPaint);
            mPaint.setXfermode(null);
            c.restoreToCount(layerId);
            /**
             * 第一次进去的时候发现画出来的东西很奇怪 暂未找到原因
             * 需要滑动之后才会正常显示，所以第一次进去的时候，手动调用一次刷新
             */
            if (recycleViewfirstComeInFlag == false) {
                recycleViewfirstComeInFlag = true;
                parent.postInvalidate();
            }
        }


    };


    private fun test(){
         val flow = flow<String> {
            emit("1231")

        }.takeWhile {
            true
         }




    }

}