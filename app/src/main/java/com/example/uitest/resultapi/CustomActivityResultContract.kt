package com.example.uitest.resultapi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.activity.result.contract.ActivityResultContract
import com.example.uitest.MainActivity3

class CustomActivityResultContract : ActivityResultContract<String, String>() {

     companion object{
         val value_key = "value_key"
     }

    override fun createIntent(context: Context, input: String?): Intent {
        val intent = Intent(context,MainActivity3::class.java)
        intent.putExtra(value_key,input)
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String {
        val value = intent?.getStringExtra(value_key);
        if (value != null && resultCode == Activity.RESULT_OK){
            return value;
        }
        return ""
    }
}