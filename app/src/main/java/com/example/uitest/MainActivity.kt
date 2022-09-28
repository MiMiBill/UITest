package com.example.uitest

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Choreographer
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.Target
import com.example.uitest.resultapi.CustomActivityResultContract
import com.example.uitest.spannableString.buildSpannableString
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.*
import okhttp3.*
import okhttp3.internal.immutableListOf
import java.io.File
import java.io.IOException
import java.lang.ref.ReferenceQueue
import java.util.*
import kotlin.concurrent.thread
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class MainActivity : AppCompatActivity() {

    lateinit var name:String;
    lateinit var imageVIew:ImageView
    lateinit var ivSceenCap:ImageView
    lateinit var moon:MyMoon
    private val handle = Handler(Looper.myLooper()!!)
    val scope = CoroutineScope(Dispatchers.Main);
    lateinit var activityResultLauncher:ActivityResultLauncher<String>
    var p :Person1? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageVIew = findViewById(R.id.iv_imageview)

        ivSceenCap = findViewById(R.id.iv_sceenCap)
        moon = findViewById(R.id.moon)
        val toList = findViewById<TextView>(R.id.toList);
        toList.setOnClickListener {
            Intent(MainActivity@this,ListActivity4::class.java).apply {
                startActivity(this)
            }
        }
        findViewById<View>(R.id.to_mainActivity4).setOnClickListener {
            Intent(MainActivity@this,MainActivity4::class.java).apply {
                startActivity(this)
            }
        }

        toList.buildSpannableString {
            addText("测试"){
                setColor("#FF0000")
                onClick {
                    Toast.makeText(baseContext,"hello",Toast.LENGTH_SHORT).show()
                }
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

        ivSceenCap.setOnDebounceClick {

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
        initDsl();
        testLiveData();
        scope.launch {
            testFlow();
        }
        testCoroutineScope()

        val thread = thread(start = true) {
            Log.d("testFlow","thread run")
//            val ee = 1/0;
        }
        thread.setUncaughtExceptionHandler { t, e ->
            Log.d("testFlow","thread:" + e.message )
        }

        testChannel();
        getMemoryInfo(this)
        initChoreographer()
    }


    private val  callBack:Choreographer.FrameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            "刷新了${frameTimeNanos}".logD()
            Choreographer.getInstance().postFrameCallback(this)
        }
    }

    private fun initChoreographer() {
        Choreographer.getInstance().postFrameCallback(callBack)
    }


    fun getMemoryInfo(context: Context) {
        try {
            val TAG = "getMemoryInfo";
            val rt = Runtime.getRuntime()
            val maxMemory = rt.maxMemory()
            Log.e(TAG, "MaxMemory:" + java.lang.Long.toString(maxMemory / (1024 * 1024)))
            val activityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            Log.e(
                TAG,
                "MemoryClass:" + java.lang.Long.toString(activityManager.memoryClass.toLong())
            )
            Log.e(
                TAG,
                "LargeMemoryClass:" + java.lang.Long.toString(activityManager.largeMemoryClass.toLong())
            )
            Log.e(TAG, "widthPixels:" + context.getResources().getDisplayMetrics().widthPixels)
            Log.e(
                TAG,
                "heightPixels:" + context.getResources().getDisplayMetrics().heightPixels
            )
            val info = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(info)
            Log.e(TAG, "系统剩余内存:" + (info.availMem shr 10) / 1024 + "m")
            Log.e(TAG, "系统是否处于低内存运行：" + info.lowMemory)
            Log.e(TAG, "当系统剩余内存低于" + (info.threshold shr 10) / 1024 + "m时就看成低内存运行")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun testChannel() {
        val  chanel = Channel<String>(2)
        scope.launch {
            for (value in 0..9){
                delay(100)
                chanel.send("发送：$value")
                Log.d("testChannel","发送:" + value)
                if (value == 5){
                    chanel.cancel()
                }
            }
            Log.d("testChannel","发送结束----！")

        }

        scope.launch {

            repeat(20){
                delay(500)
                Log.d("testChannel","接收:" + chanel.receive())
            }
            Log.d("testChannel","接收结束--------！")

        }


        val channel = GlobalScope.produce<String> {


        }

        scope.launch {
            for (value in channel){


            }
        }
//        File("").forEachBlock(1)
        applicationInfo.nativeLibraryDir
    }

    private fun testCoroutineScope() {
        runBlocking {
            Log.d("testCoroutineScope","testCoroutineScope0:" + Thread.currentThread())
            delay(1000)
            launch {
                Log.d("testCoroutineScope","testCoroutineScope1:" + Thread.currentThread())
                delay(2000)
                Log.d("testCoroutineScope","testCoroutineScope2:" + Thread.currentThread())
            }

            launch {
                Log.d("testCoroutineScope","testCoroutineScope3:" + Thread.currentThread())
                delay(2000)
                Log.d("testCoroutineScope","testCoroutineScope4:" + Thread.currentThread())
            }
            try {
                val result = suspendTest();
                Log.d("testCoroutineScope", "suspendTest:$result")
            }catch (e:Exception){
                Log.d("testCoroutineScope","suspendTest Exception:" + e.message)
            }
            Log.d("testCoroutineScope","testCoroutineScope5:" + Thread.currentThread())

        }





        val  job = Job()
        val coroutine =  CoroutineScope(job)
        coroutine.launch {

            val  ret = withContext(Dispatchers.IO){
                delay(1000)
                Log.d("testCoroutineScope","testCoroutineScopeRet:" + Thread.currentThread())
                12313123
            }
            Log.d("testCoroutineScope","testCoroutineScopeRet:" + ret)
        }
        Log.d("testCoroutineScope","testCoroutineScope6:" + Thread.currentThread())



    }


    private suspend fun suspendTest():String = suspendCoroutine {
        it.resumeWithException(IllegalArgumentException("参数错误"))
    }

    private suspend fun testFlow() {

        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.d("testFlow","协程异常：" + throwable)
        }
        GlobalScope.launch(exceptionHandler) {
                1/0
        }


        try {
            //如果block中执行时间超过了10毫秒，那么就会抛出异常，如果不捕获异常，那么当下的整个协程都会停止
            val ret = withTimeout(10){
                Log.d("testFlow","超时block")
                delay(200)
                "1231"
            }
        }catch (e:Exception){
            Log.d("testFlow","超时开始:" + e.message)

        }


        //https://blog.csdn.net/qq_39037047/article/details/121889740
        //https://www.jianshu.com/p/34cdf688920f
        val stateFlow = MutableStateFlow <String>("MutableStateFlow初始化")
        val emptyFlow = emptyFlow<String>()

        var flow = flow<String> {
            Log.d("testFlow","发送数据中....")
            emit("hello1")
            emit("hello2")
            emit("hello3")
        }.map {
            it
        }
        handle.postDelayed({
            scope.launch {
                flow.collect {
                    Log.d("testFlow",it)
                }
                stateFlow.value = "新值"
            }
        },1000)



        val job = scope.launch {
            stateFlow.collect {
                Log.d("testFlow","stateFlow:" + it)
            }

            delay(1)
            async(start = CoroutineStart.LAZY) {

            }


        }


        val list = ArrayList<String>()
        list.asFlow()

        immutableListOf("")

        scope.launch {
            flow.collectIndexed { index, value ->
                Log.d("testFlow","stateFlow:" + index + " value:" + value)
            }
        }





        val array = arrayListOf<String>();
        scope.launch {
            flow.toCollection(array)
            array.forEach {
                Log.d("testFlow","toCollection:" + it)
            }

            val result = flow.fold("") { resut, value ->
                resut + value
            }


//            flow = flow.onStart {
//                Log.d("testFlow","onStart")
//            }
//
//            flow = flow.onCompletion {
//                Log.d("testFlow","onCompletion")
//            }
            Log.d("testFlow","toCollection result:" + result)
            flow.toList().forEach {
                Log.d("testFlow","toList:" + it)
            }


        }


        flow = flow.onEach {
            Log.d("testFlow","onEach:" + it)
        }

        scope.launch {
            flow.flowOn(Dispatchers.Main).map {
                "map+" + it
            }.collect {
                Log.d("testFlow","map:" + it)
            }

           val reduce = flow.reduce { accumulator, value ->
                accumulator + value
            }
            Log.d("testFlow","reduce:" + reduce)
        }

        val reduce = flow.reduce { accumulator, value ->
            accumulator + value
        }



    }

    private fun testLiveData() {
        val  send = MutableLiveData<String>();
        val  send1 = MutableLiveData<String>()
        val mediatorLiveData = MediatorLiveData<String>()


        val  receive =  Transformations.map(send,{
            it + "已经装换了"
        })

        val get = fun(it: String){
            Log.d("mediatorLiveData","内容:" +it)
            mediatorLiveData.value =  "收集后二次发送:${it}"
        }

        val switch = Transformations.switchMap(send,{
            send1
        })

        mediatorLiveData.addSource(receive,get)
        mediatorLiveData.addSource(switch,get)

        mediatorLiveData.observe(this,object :androidx.lifecycle.Observer<String>{
            override fun onChanged(t: String?) {
                Log.d("mediatorLiveData","observe 内容" +t)
            }

        })
        send.value = "第一次发送"
        send1.value = "第二个发送"
        scope.launch {
            delay(4000)
            send.value = "第3次发送"
            send1.value = "第4个发送"
            Log.d("mediatorLiveData","来到了协程")
        }


//        val m = MediaPlayer();
//        val  playbackParams = PlaybackParams()
//        m.playbackParams = playbackParams
//        playbackParams.setSpeed()

        val  link = LinkedList<String>();


    }



    private fun initDsl() {
        var  inout = findViewById<EditText>(R.id.input);
        inout.addTextChangedListenerDsl {
            afterTextChanged {
                Log.i("addTextChangedListener",it.toString());
            }

            beforeTextChanged{ str,_,_,_->
                Log.i("addTextChangedListener","beforeTextChanged${str}");
            }
        }



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
            .observeOn(Schedulers.newThread()).subscribe(object : io.reactivex.rxjava3.core.Observer<String> {
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














