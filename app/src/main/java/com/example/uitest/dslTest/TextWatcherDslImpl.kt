package com.example.uitest.dslTest

import android.text.Editable
import android.text.TextWatcher

class TextWatcherDslImpl : TextWatcher {

    private var afterTextChanged :((s: Editable?)->Unit)? = null;
    private var beforeTextChanged:((s: CharSequence?, start: Int, count: Int, after: Int) ->Unit)? = null;
    private var onTextChanged:((s: CharSequence?, start: Int, before: Int, count: Int)-> Unit)? = null;



    /**
     * DSL中使用的函数，一般保持同名即可
     */
    fun afterTextChanged(method: (Editable?) -> Unit) {
        afterTextChanged = method
    }

    fun beforeTextChanged(method: (CharSequence?, Int, Int, Int) -> Unit) {
        beforeTextChanged = method
    }

    fun onTextChanged(method: (CharSequence?, Int, Int, Int) -> Unit) {
        onTextChanged = method
    }


    override fun afterTextChanged(s: Editable?) {
        afterTextChanged?.invoke(s);
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        beforeTextChanged?.invoke(s, start, count, after)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onTextChanged?.invoke(s, start, before, count);
    }
}