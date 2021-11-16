package com.example.uitest

import android.app.Activity
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.uitest.resultapi.CustomActivityResultContract
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.TriangleEdgeTreatment
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.ExplainReasonCallback
import com.permissionx.guolindev.callback.RequestCallback
import com.permissionx.guolindev.dialog.DefaultDialog
import com.permissionx.guolindev.request.ExplainScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.io.*

class MainActivity3 : AppCompatActivity() {


    lateinit var imageView: ImageView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        imageView = findViewById(R.id.imageView)

        val value =  CustomActivityResultContract().parseResult(Activity.RESULT_OK,intent)
        Log.d("testResultApi","value:${value}")

        CoroutineScope(Dispatchers.Main).launch {
            testFlow()
        }
        testShapeAppearanceModel()
        testFile()
        testBlock()





    }

    private fun testBlock() {

        val value = processStr { this + "555" }
        Log.d("testBlock","testBlock:${value}")

        testOnLisenter { onSuccess {
            Log.d("testBlock","调用里面打印1")
        } }

        val block :(String.()->Unit) = {
            Log.d("testBlock","打印string:${this}")
        }

        val test = "12312".also {
            block
        }

    }


    private fun testOnLisenter(listener:onLisenter.()->Unit){
        val onLisenter =  onLisenter().also{
            it.onSuccess {
                Log.d("testBlock","调用里面打印2")
            }
        }
        onLisenter.success?.invoke()
    }


    fun processStr(process:String.()->String):String{
        return process.invoke("123")
    }


    private class onLisenter{
        var success:(()->Unit)? = null
        fun onSuccess(success:()->Unit){
            this.success = success
            Log.d("testBlock","onLisenter onSuccess")
        }
    }


    private fun testFile() {
          val getDownloadCacheDirectory = Environment.getDownloadCacheDirectory();
        Log.d("testFile","getDownloadCacheDirectory:${getDownloadCacheDirectory.absolutePath}")
        val getExternalStorageDirectory = Environment.getExternalStorageDirectory();
        Log.d("testFile","getExternalStorageDirectory:${getExternalStorageDirectory.absolutePath}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val getStorageDirectory = Environment.getStorageDirectory()
            Log.d("testFile","getStorageDirectory:${getStorageDirectory.absolutePath}")
        }
        val getExternalFilesDir = MyApp.context.getExternalFilesDir(null);
        Log.d("testFile","getExternalFilesDir:${getExternalFilesDir?.absolutePath}")
        val filesDir = MyApp.context.filesDir;
        Log.d("testFile","filesDir:${filesDir.absolutePath}")
        val getCodeCacheDir = ContextCompat.getCodeCacheDir(this);
        Log.d("testFile","getCodeCacheDir:${getCodeCacheDir.absolutePath}")

        val getDataDir = ContextCompat.getDataDir(this);
        Log.d("testFile","getDataDir:${getDataDir?.absolutePath}")

        var times = 0
        PermissionX.init(this).permissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA)
            .explainReasonBeforeRequest()
            .onExplainRequestReason(object :ExplainReasonCallback{
                override fun onExplainReason(scope: ExplainScope, deniedList: MutableList<String>) {
                    val defaultDialog = DefaultDialog(this@MainActivity3,deniedList,"这是一个存储权限","确定","取消",Color.BLUE,Color.RED)
//                    times ++;
//                    if (times >2)return
                    scope.showRequestReasonDialog(dialog = defaultDialog)
//                    if (hasRequest)return
//                    scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "我已明白","不了")
                }

            }).onForwardToSettings { scope, deniedList ->
                val message = "您需要去设置中手动开启以下权限"
                val dialog = DefaultDialog(this@MainActivity3, deniedList, "需要权限","确定","取消",Color.RED,Color.BLUE)
                scope.showForwardToSettingsDialog(dialog)
            }.request(object : RequestCallback{
            override fun onResult(
                allGranted: Boolean,
                grantedList: MutableList<String>,
                deniedList: MutableList<String>,
            ) {
                if (allGranted){
                    writeFile(getExternalFilesDir)
                }
            }

        })

        PermissionX.init(this)
        val permissionX = PermissionX.isGranted(MyApp.context,android.Manifest.permission.CAMERA)
        Log.d("PermissionX","CAMERA：${permissionX}")
        val BLUETOOTH = PermissionX.isGranted(MyApp.context,android.Manifest.permission.BLUETOOTH)
        Log.d("PermissionX","BLUETOOTH：${BLUETOOTH}")


    }


    fun writeFile(fileDir:File?){
        if (fileDir == null)return
        val fileName = "log.txt"
        val filePath = fileDir.absolutePath + File.separator + fileName
        val file = File(filePath)
        if (!file.exists()){
            if (!file.createNewFile()){
                Log.d("testFile","创建文件失败：${filePath}")
             return
            }
        }

        val fileOutputStream = FileOutputStream(file);
        val txt = "写入文件"
        fileOutputStream.write(txt.toByteArray(Charsets.UTF_8))
        fileOutputStream.close()

        val bufferedReader = BufferedReader(FileReader(filePath))
        Log.d("testFile","读取的内容：${bufferedReader.readLine()}")

        val apkUri: Uri =
            FileProvider.getUriForFile(MyApp.context, "com.example.uitest.fileprovider", file)
        Log.d("testFile","apkUri：${apkUri}")

    }

    fun testShapeAppearanceModel(){
        val shapeAppearanceModel = ShapeAppearanceModel.builder()
            .setAllCorners(CornerFamily.ROUNDED,50f)
            .setAllEdges(TriangleEdgeTreatment(30f, true))
            .build()
        val materialShapeDrawable = MaterialShapeDrawable(shapeAppearanceModel).apply {
            setTint(Color.RED)
            paintStyle = Paint.Style.FILL
        }
        imageView.background = materialShapeDrawable
    }


     suspend fun testFlow(){

        flow{
            Log.d("testFlow","thread flow:" + Thread.currentThread())
            emit(123)
            delay(2000)
            emit(234)
        }.flowOn(Dispatchers.Main).collect {
            Log.d("testFlow","thread collect:" + Thread.currentThread() + "  value:${it}")
        }

    }



    override fun onBackPressed() {

        setResult(Activity.RESULT_OK,CustomActivityResultContract().createIntent(this,"hahahah"))
        super.onBackPressed()

    }
}