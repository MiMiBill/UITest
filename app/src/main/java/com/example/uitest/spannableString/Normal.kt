package com.example.uitest.spannableString

import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView

/**
@author:longyun
@email yun.long@ximalaya.com
@date:2022/9/21
@des:
 */

interface DslSpannableStringBuilder {
    //增加一段文字
    fun addText(text: String, method: (DslSpanBuilder.() -> Unit)? = null)
}

interface DslSpanBuilder {
    //设置文字颜色
    fun setColor(color: String)
    //设置点击事件
    fun onClick(useUnderLine: Boolean = true, onClick: (View) -> Unit)
}

//为 TextView 创建扩展函数，其参数为接口的扩展函数
fun TextView.buildSpannableString(init: DslSpannableStringBuilder.() -> Unit) {
    //具体实现类
    val spanStringBuilderImpl = DslSpannableStringBuilderImpl()
    spanStringBuilderImpl.init()
    movementMethod = LinkMovementMethod.getInstance()
    //通过实现类返回SpannableStringBuilder
    text = spanStringBuilderImpl.build()
}