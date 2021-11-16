package com.example.uitest

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewConfiguration
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.view.ViewConfigurationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.Target
import com.example.uitest.resultapi.CustomActivityResultContract
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.*
import java.io.FileNotFoundException
import java.io.IOException
import java.lang.ref.ReferenceQueue
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference
import java.net.UnknownHostException
import java.util.function.Consumer


class MainActivity : AppCompatActivity() {

    lateinit var name:String;
    lateinit var imageVIew:ImageView
    lateinit var ivSceenCap:ImageView
    lateinit var moon:MyMoon
    lateinit var activityResultLauncher:ActivityResultLauncher<String>
    var p :Person1? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageVIew = findViewById(R.id.iv_imageview)
        val scope = CoroutineScope(Dispatchers.Main);
        ivSceenCap = findViewById(R.id.iv_sceenCap)
        moon = findViewById(R.id.moon)
        findViewById<View>(R.id.toList).setOnClickListener {
            Intent(MainActivity@this,ListActivity4::class.java).apply {
                startActivity(this)
            }
        }

        findViewById<View>(R.id.toWave).setOnClickListener {
            Intent(MainActivity@this,WaveActivity4::class.java).apply {
                startActivity(this)
            }
        }


        scope.launch {
            Log.d("MainActivity","scope thread:" + Thread.currentThread())
          val  value1 = async {
              delay(1000)
              Log.d("MainActivity","scope async thread:" + Thread.currentThread())
                1 + 3;
            }
            val value2 = async { 1 + 4 }
            val  all = value1.await() + value2.await()
            Log.d("MainActivity","value:" + all)
            val  data = getData("")
            Log.d("MainActivity","data:" + data)

            getFlow().map { it + 3 }.take(2).collect {
                Log.d("MainActivity","collect:" + it)
            }
        }
        SynchronizedTest()
        val sex:Sex = Sex.Woman()
        val s = when (sex) {
            is Sex.Man -> {
                Log.d("MainActivity", "man")
                "man"
            }
            is Sex.Woman -> "woman"
        }
        Log.d("MainActivity", "sex:" + s)

        val person = Person("long",18).apply { name = "wang"
                                                            age = 10


                                                   }

        val str:Person = person.let { name = ""
            it
        }



        val (name,age) = person
        Log.d("MainActivity", "Person:name:${name} age:${age}")

        ivSceenCap.setOnClickListener {
//            val intent = Intent(this@MainActivity,MainActivity2::class.java)
//
//            val array = arrayListOf<Byte>()
//            for (index in 0..(50 * 1024).toInt()){
//                array.add(0x11)
//            }
//            intent.putExtra("by",array)
//            startActivity(intent)
            startResultActivity()
        }

        testGlide()
        testOkhttp()
        testWeakrefrence()
        testRxjava()
        testScreenCap()
        testToast()
        testSnakbar()
        testTap()
        testSp()
        initResultApi()
    }

    fun startResultActivity(){
        activityResultLauncher.launch("你好，我来自${MainActivity::class.java.simpleName}")
    }



    fun initResultApi(){
        activityResultLauncher = registerForActivityResult(CustomActivityResultContract()){
            Log.d("testResultApi","value:${it}")
        }

    }

    override fun onStop() {
        super.onStop()
    }

    private fun testSp() {
        getSharedPreferences("hello",0).edit().putBoolean("hahah",false).commit()
    }

    private fun testTap() {
        imageVIew.setOnClickListener {
            Log.d("testTap","testTap setOnClickListener")
        }

        imageVIew.setOnLongClickListener {
            Log.d("testTap","testTap setOnLongClickListener")
            return@setOnLongClickListener true
        }

    }


    private fun testSnakbar() {
        "this is a snakebar".showSnackbar(moon)
    }

    private fun testToast() {
        for (index in 1..8){
            "这是一个toast:${index}".toast()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "getStatusHeight:${Utils.instance.getStatusHeight(this)}")

    }


    fun testScreenCap(){
//        val  bigMap = Utils.instance.screenCapByDrawing(window.decorView)
        moon.post({
            val  bigMap = Utils.instance.screenCapCarvas(moon)
            ivSceenCap.setImageBitmap(bigMap)
        })
//        window.decorView.visibility = View.VISIBLE
    }


    fun testRxjava(){

//        RxJavaPlugins.setOnObservableAssembly(object : @io.reactivex.rxjava3.annotations.Nullable io.reactivex.rxjava3.functions.Function<Observable<Any>, Observable<Any>> {
//            override fun apply(t: Observable<Any>?): Observable<Any> {
//
//                t!!.subscribe({
//                    Log.d("testRxjava", "setOnObservableAssembly:${it}")
//                })
//
//                return t!!
//            }
//
//        } )


        RxJavaPlugins.setErrorHandler(object :
            @io.reactivex.rxjava3.annotations.Nullable io.reactivex.rxjava3.functions.Consumer<Throwable> {
            override fun accept(t: Throwable?) {

            }
        })

        val subscribe = Observable.just("name").map { it + "haah" }.subscribe({
            Log.d("testRxjava", "testRxjava:${it}")
        })


        Observable.create(object :ObservableOnSubscribe<String>{
            override fun subscribe(emitter: ObservableEmitter<String>?) {
                emitter?.onNext("你好")
                emitter?.onError(Exception("hhhh"))
                emitter?.onComplete()
//                emitter?.onError(IOException("hhhh"))
//                emitter?.onComplete()
            }
        }).map { it + " 在哪里？" }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread()).subscribe(object : Observer<String> {
            override fun onSubscribe(d: Disposable?) {
                Log.d("testRxjava", "onSubscribe")
            }

            override fun onNext(t: String?) {
                Log.d("testRxjava", "onNext")
            }

            override fun onError(e: Throwable?) {
                Log.d("testRxjava", "onError")
            }

            override fun onComplete() {
                Log.d("testRxjava", "onComplete")
            }

        })
    }

     fun testWeakrefrence(){
         val  queue = ReferenceQueue<Person1>()
         p = Person1();
         val ref = MyWeakReference<Person1>(p!!,queue, p!!.javaClass.simpleName);
         Log.d("testWeakrefrence", "testWeakrefrence1:${ref.get()}")
         p = null;
         Log.d("testWeakrefrence", "testWeakrefrence2:${ref.get()}")
         val coroutine =  CoroutineScope(Dispatchers.Main)
         coroutine.launch {
             System.gc()
             delay(4000)
             Log.d("testWeakrefrence", "testWeakrefrence3:${ref.get()}")
             var any = queue.poll()
//             while (any == null){
//                 delay(2000)
//                 any = queue.poll()
//             }
             Log.d("testWeakrefrence", "testWeakrefrence4:${any}")

         }




     }


    fun testOkhttp(){
        val okHttpClient = OkHttpClient()

        val request = okhttp3.Request.Builder().url("https://square.github.io/okhttp/#get-a-url").get().build()
        okHttpClient.newCall(request).enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.d("testOkhttp", "onFailure:${e.toString()}")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("testOkhttp", "onResponse:${response.body?.string()}")
            }

        })
    }

    fun testGlide(){

        var  customViewTarget = object:DrawableImageViewTarget(imageVIew){
            override fun onStart() {
                super.onStart()
                Log.d("testGlide","onStart")
            }

            override fun onStop() {
                super.onStop()
                Log.d("testGlide","onStop")

            }

        }

        Glide.with(imageVIew.context)
            .load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fnimg.ws.126.net%2F%3Furl%3Dhttp%253A%252F%252Fdingyue.ws.126.net%252F2021%252F0611%252F2e84f869j00qujnx3001nc000hs00hsc.jpg%26thumbnail%3D650x2147483647%26quality%3D80%26type%3Djpg&refer=http%3A%2F%2Fnimg.ws.126.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1634979017&t=347808fd2c222f3f2f8bf88aaee35597")
//            .load("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=glide%20BitmapImageViewTarget&fenlei=256&oq=glide%2520%2526lt%253BustomViewTarget&rsv_pq=c5c5015a0000dc70&rsv_t=de66tQKasOnyeWKeack%2BGGnVvtJ75UuYTHsjnUoubWNvtgs6jIHUibaMNf0&rqlang=cn&rsv_enter=1&rsv_dl=tb&rsv_btype=t&inputT=7114&rsv_n=2&rsv_sug2=0&rsv_sug4=7148")
            .listener(object :RequestListener<Drawable>{

                /**
                 * 返回true：消耗事件，错误加载的图片不会显示到页面上，需要自己操作，这时候可以根据不同的错误类型显示不同的图片
                 * 返回false，不消耗事件，错误图片会正确加载到页面上
                 */
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d("testGlide","error:${e.toString()}")
                    Log.d("testGlide","target:${target?.javaClass?.name}")
                    Log.d("testGlide","model:${model?.javaClass?.name} value:${model}")
                    if (target is DrawableImageViewTarget){
                        Log.d("testGlide"," target.view:${ target.view?.javaClass?.name}")
                        if (e.toString().contains("UnknownHostException")){
                            target.view.setImageResource(R.mipmap.meinv)
                        }else if (e.toString().contains("FileNotFoundException")){
                            target.view.setImageResource(R.mipmap.ic_launcher_round)
                        }
                    }
                    return true
                }

                /**
                 * 返回true：消耗事件，加载的图片不会显示到页面上，需要自己操作
                 * 返回false，不消耗事件，图片会正确加载到页面上
                 */
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean,
                ): Boolean {
                    return false
                }

            })
            .error(R.mipmap.ic_launcher)
            .placeholder(R.mipmap.loading)
//            .into(imageVIew)
            .into(customViewTarget)
        Log.d("testGlide",imageVIew.context.toString())

    }

    suspend fun getData(value:String):String{
        Log.d("MainActivity","getData in")
        delay(1000);
        Log.d("MainActivity","getData out")
        return "data"
    }




    fun getFlow(): Flow<Int> {
        return flow {
            println("Flow started")
            for (i in 1..5) {
                delay(1000)
                emit(i)
            }
        }
    }

    @Synchronized fun SynchronizedTest(){

        val name = "long"
        val name1= "long"
        Log.d("MainActivity","SynchronizedTest:${name == name1}")
        Log.d("MainActivity","SynchronizedTest:${name.compareTo(name1)}")
        Log.d("MainActivity","SynchronizedTest:${name.isEmpty()}")




    }



    @Volatile var  value = 1




}













