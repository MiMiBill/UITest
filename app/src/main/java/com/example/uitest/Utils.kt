package com.example.uitest

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import okhttp3.internal.wait

class Utils private constructor(){

    companion object{
        val instance:Utils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Utils()
        }
    }


    public fun screenCapByDrawing(view:View) :Bitmap{
        view.measure(View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED),View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED));
        view.layout(0,0,view.measuredWidth,view.measuredHeight)
        val width = view.width;
        val heiht = view.height
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache()
        val bitmap =  view.getDrawingCache()
        return bitmap
    }


      fun screenCapCarvas(view: View):Bitmap{
         val bitmap = Bitmap.createBitmap(view.width,view.height,Bitmap.Config.RGB_565)
         val canvas = Canvas(bitmap)
         view.draw(canvas)
         return bitmap
    }

    fun getStatusHeight(context: Context):Int{
        var result = 0;
        val resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }





}