package com.example.uitest

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator

class WaveLayout:View {

    val INVALIDATE_MSG = 1001
    var widh = 0;
    val minWidth = 1f.dp2Px();
    var halfWidth = 0f;
    var delay = 3000L;
    val startDelay = 600;
    var circleNum = 1;
    var strokeWidth = 3f.dp2Px().toFloat()
    lateinit var linePaint:Paint
    var animatorList = arrayListOf<ValueAnimator>();
    val handle = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == INVALIDATE_MSG){
                invalidate()
            }
        }
    }

    constructor(context: Context):super(context)

    constructor(context: Context,attrs:AttributeSet):super(context, attrs)

    init {
        strokeWidth = 2f.dp2Px().toFloat()
        linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = Color.parseColor("#32CD99")
            this.isDither = false;
            this.style = Paint.Style.STROKE
            this.strokeWidth = 2f.dp2Px().toFloat()
        }
    }

    fun makeValueAnimatorList(){
        circleNum = (delay / startDelay).toInt()
        delay = (circleNum * startDelay).toLong();
        for (value in 0..circleNum){
            animatorList.add(makeValueAnimator((value * startDelay).toLong()))
        }
    }

    private fun makeValueAnimator(startDelay:Long):ValueAnimator{
        return ValueAnimator().apply {
            this.setFloatValues(minWidth.toFloat(),halfWidth)
            this.repeatCount = -1;
            this.duration = delay
            this.startDelay = startDelay
            this.interpolator = DecelerateInterpolator()
            start()
        }

    }


    override fun onFocusChanged(gainFocus: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)



    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        widh = (Math.min(w,h) - 2 * strokeWidth).toInt();
        halfWidth = (widh /2).toFloat()
        makeValueAnimatorList()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val iterator = animatorList.iterator()
        while (iterator.hasNext()){
            val  next = iterator.next();
            if (next is ValueAnimator){
               val value = next.animatedValue as Float
                linePaint.alpha = 255 - (next.animatedFraction * 255).toInt()
                Log.d("WaveLayout","linePaint.alpha:${linePaint.alpha}" )
                canvas?.drawCircle(halfWidth,halfWidth,value,linePaint)
            }
        }
        handle.postDelayed({
            invalidate()
        },10);


    }





}