package com.example.uitest

import android.graphics.Color
import android.os.Bundle
import android.text.TextPaint
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import androidx.work.Data
import com.opensource.svgaplayer.*
import java.util.concurrent.TimeUnit

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val meinv = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg5.51tietu.net%2Fpic%2F2019-083010%2Fvvpiimlamdxvvpiimlamdx.jpg&refer=http%3A%2F%2Fimg5.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1636359251&t=5e3e590e07bb5ec2fae7abd5f87c1308"

        val svga = findViewById<SVGAImageView>(R.id.svga)
        SVGAParser(this).decodeFromAssets("lover.svga",object : SVGAParser.ParseCompletion{
            override fun onComplete(videoItem: SVGAVideoEntity) {

                val svgaVideoEntity = SVGADynamicEntity();

                val textPaint = TextPaint()
                textPaint.color = Color.WHITE //字体颜色
                textPaint.textSize = 24f //字体大小
                textPaint.setShadowLayer(3f, 2f, 2f, -0x1000000) //字体阴影，不需要可以不用设置


                for (index in 227..336){
                    svgaVideoEntity.setDynamicText("我是美女",
                        textPaint,
                        "img_${index}")
                }

                val svgaDrawable = SVGADrawable(videoItem,svgaVideoEntity)
                svga.setImageDrawable(svgaDrawable)
                svga.stepToFrame(0,true)
            }

            override fun onError() {
            }

        })


        testWorker()


    }

    private fun testWorker() {

        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val  data = Data.Builder().putString("data","我是外面传入的数据").build()
        val oneTimeWorkRequest = OneTimeWorkRequest
            .Builder(WorkerProcess::class.java)
            .setConstraints(constraints)
            .setInitialDelay(1,TimeUnit.SECONDS)
            .setInputData(data)
            .build()

//        val periodicWorkRequest = PeriodicWorkRequest.Builder(WorkerProcess::class.java,15,TimeUnit.MINUTES).setInputData(data).setInitialDelay(4,TimeUnit.SECONDS).build()
        WorkManager.getInstance(this).enqueue(oneTimeWorkRequest);
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(oneTimeWorkRequest.id).observe(this,{
            Log.d("WorkerProcess","get getWorkInfoByIdLiveData:${it.state}")
            if (it.state == WorkInfo.State.SUCCEEDED){
                val data = it.outputData.getString("data")
                Log.d("WorkerProcess","get getWorkInfoByIdLiveData:${data}")
            }
        })
    }
}