package com.example.uitest

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.uitest.dslTest.TextWatcherDslImpl
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

fun String.isEmpty():Boolean{
    return TextUtils.isEmpty(this)
}

infix fun String.endWith(value:String):String{
    return this + value;
}


fun Int.dp2Px():Int{
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(), MyApp.context.resources.displayMetrics).toInt()
}


fun Float.dp2Px():Int{
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        this, MyApp.context.resources.displayMetrics).toInt()
}


fun String.name(){

}



var mToast:Toast? = null

fun String.toast(){
    mToast =  mToast?: Toast.makeText(MyApp.context,this,Toast.LENGTH_SHORT)
    mToast?.setText(this)
    mToast?.show()
    Log.d("toast","toast:${mToast}")
}


fun String.logD(tag:String = ""){
    Log.d(tag,this)
}

fun String.showSnackbar(view:View){
    val snackbar =  Snackbar.make(view,this,Snackbar.LENGTH_SHORT)
   snackbar.show()
}

public fun main(args:Array<String>){
//    val value = "long" endWith "yun"
//    println(value)
//
//    val age = lazy { 18 }
    //例子：
//    val arr = arrayListOf(4,2,4).toIntArray()
//    print(maxWater(arr))
    //例子：
//    println(getTargetIndex1(7).toString())
//    hookTest()
    testLet()


}


fun testLet() {
    val letVal = Person1().let {
        it.mFood = "好吃的"
        it
    }
    letVal.eat()




    val withVal = with(Person1(),{
        mFood = "ping guo"
        eat()
    })
    println(withVal)

    val runVal = Person1().run {
        mFood = "梨"
        eat()
    }
    println(runVal)

    val applyVal = Person1().apply {
        ""
    }

    println(applyVal)


    repeat(10,{
        println("testLet:${it}")
    })

}


fun hookTest(){
    var  person1 = Person1();
    val person = hook(person1)
    person.eat("冰激凌")
    person.cry()
}


fun hook(person: Person1): IPeople {
    val classList = arrayOf<Class<*>>(IPeople::class.java)
    val proxyInstance = Proxy.newProxyInstance(person.javaClass.classLoader, classList,object : InvocationHandler{
        override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any {
            println("hook method:${method} args:${args}")
            method?.invoke(person, *args.orEmpty())
            return proxy!!
        }
    })
    return proxyInstance as IPeople;

//    val proxyInstance = Proxy.newProxyInstance(person.javaClass.getClassLoader(), classList
//    ) {
//            proxy, method, args ->
//        println("hook method:${method}")
//        if (method.getName().equals("put")) {
//            println("hook method:${method}")
//        }
//        method.invoke(person, args)
//        proxy
//    }
//    return proxyInstance as People;

}



/**
 * 算法，给定一个有序不重复数组，使用时间复杂度小于N方的方法，找到所有两两相加等于target值的组合:
int[] a={1,2,3,4,5,6,7,8}
int target=7
 * **/

public fun getTargetIndex(target: Int): ArrayList<Int>{
    var retArray = arrayListOf<Int>();
    val map = mutableMapOf<Int,Int>()
    val array = arrayListOf<Int>(1,2,3,4,5,6,7,8)
    for ((index,value) in array.withIndex()){
        map.put(value,index)
    }
    return retArray;
}

public fun getTargetIndex1(target: Int): ArrayList<Int>{
    var retArray = arrayListOf<Int>();
    val array = arrayListOf<Int>(1,2,3,4,5,6,7,8)
    var indexLow = 0;
    var indexHigh = array.size - 1
    while (indexHigh > indexLow){
        println("------------")
        if (array[indexLow] + array[indexHigh] > target){
            indexHigh --
        }else if (array[indexLow] + array[indexHigh] < target){
            indexLow ++
        }else{
            retArray.add(indexLow)
            retArray.add(indexHigh)
            indexLow ++
            indexHigh --
        }
    }
    return retArray;
}

/**
 * 算法：牛客网上的题目
 */
fun maxWater(arr: IntArray): Long  {
    // write code here
    if (arr == null || arr.size < 3)return 0
    var left = 0
    var right = arr.size - 1;
    var mark = Math.min(arr[left],arr[right]);
    var sum = 0L
    while (left < right){

        if (arr[left] < arr[right]){
            left ++
            if (mark > arr[left]){
                sum += mark - arr[left]
            }else{
                mark = Math.min(arr[left],arr[right]);
            }

        }else{
            right --
            if (mark > arr[right]){
                sum += mark - arr[right]
            }else{
                mark = Math.min(arr[left],arr[right]);
            }
        }

    }
    return sum

}




data class Person(var name:String,var age:Int){
    val string:String
    get() {
        return "${name}${age}"
    }
}



fun TextView.addTextChangedListenerDsl(process: TextWatcherDslImpl.() -> Unit) {
    val listener = TextWatcherDslImpl()
    listener.process()
    this.addTextChangedListener(listener)
}


fun EditText.textChangeFlow(): Flow<CharSequence?> = callbackFlow {
    val watch = object :TextWatcher{
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(data: CharSequence?, start: Int, before: Int, count: Int) {
            offer(data)
        }

    }
    addTextChangedListener(watch)
    awaitClose{
        removeTextChangedListener(watch)
    }

}

fun View.setOnDebounceClick(block:(View)->Unit){
    setOnClickListener{
        if (!it.isFastClick()){
            block(this)
        }
    }
}





fun View.isFastClick():Boolean{
    val curTime = System.currentTimeMillis();
    if (curTime - this.triggerTime >= 300) {
        this.triggerTime = curTime
        return false
    } else {
        return true
    }
}


private var View.triggerTime : Long
    set(value) = setTag(R.string.track_time,value)
    get() {
        return getTag(R.string.track_time) as? Long ?: 0L
    }




