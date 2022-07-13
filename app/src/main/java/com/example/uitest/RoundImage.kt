package com.example.uitest

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View


class RoundImage : View{

    constructor(contex:Context,att:AttributeSet,def:Int = 0):super(contex,att,def)

   val mSrc = BitmapFactory.decodeResource(resources,R.mipmap.ic_launcher)
    val  meinvImg = BitmapFactory.decodeResource(resources,R.mipmap.meinv)
    constructor(contex: Context,att: AttributeSet) : this(contex,att,0)

    override fun onFinishInflate() {
        super.onFinishInflate()
        setBackgroundColor(Color.TRANSPARENT)
    }


    override fun onDraw(canvas: Canvas?) {

        val width = measuredWidth //测量宽度
        val height = measuredHeight //测量高度
        canvas?.drawBitmap(createMeinv(width, height),0f,0f,null)
        super.onDraw(canvas)

    }

    /**
     *   rvAnchorRecommend.addItemDecoration(new RecyclerView.ItemDecoration() {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
    super.onDraw(c, parent, state);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    layerId = c.saveLayer(0.0f, 0.0f, (float) parent.getWidth(), (float) parent.getHeight(), mPaint);
    }
    }

    @Override public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
    super.onDrawOver(c, parent, state);
    if (linearGradient == null) {
    linearGradient = new LinearGradient(0f,parent.getHeight()-DensityUtil.dp2px(MyApp.getInstance(),54), 0f,
    parent.getHeight()-DensityUtil.dp2px(MyApp.getInstance(),15), new int[] {Color.parseColor("#FFFFFFFF"),Color.parseColor("#00FFFFFF") },new float[]{0.2f,0.8f},
    Shader.TileMode.CLAMP);
    }
    mPaint.setXfermode(xfermode);
    mPaint.setShader(linearGradient);
    c.drawRect(0f,parent.getHeight()-DensityUtil.dp2px(MyApp.getInstance(),54),parent.getWidth(),parent.getHeight(),mPaint);
    mPaint.setXfermode(null);
    c.restoreToCount(layerId);
    /**
     * 第一次进去的时候发现画出来的东西很奇怪 暂未找到原因
     * 需要滑动之后才会正常显示，所以第一次进去的时候，手动调用一次刷新
    */
    if (recycleViewfirstComeInFlag == false) {
    recycleViewfirstComeInFlag = true;
    parent.postInvalidate();
    }
    }

    @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
    RecyclerView.State state) {
    super.getItemOffsets(outRect, view, parent, state);
    }
    });
     */


    /**
     * 镜像美女
     */
    private fun createMeinv(width: Int,height: Int):Bitmap{
        val paint = Paint(Paint.ANTI_ALIAS_FLAG);
        val meinv = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(meinv);
        val srcRectF = Rect(0, 0, meinvImg.width, meinvImg.height)
        val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat() * 0.5f)
        canvas.drawBitmap(meinvImg,srcRectF,rectF,paint);
        //倒影
        val max = Matrix();
        max.preScale(1f,-1f);
        val  reflectionImage:Bitmap = Bitmap.createBitmap(meinvImg,0,0,meinvImg.width,meinvImg.height,max,false)
        val reflectionsrcRectF = Rect(0, 0, meinvImg.width, meinvImg.height)
        val reflectionrectF = RectF(0f, height.toFloat() * 0.5f, width.toFloat(), height.toFloat())


        val linearGradient = LinearGradient(0f,
            reflectionrectF.height(), 0f,height.toFloat()
                   , 0x70ffffff, 0x00ffffff,Shader.TileMode.CLAMP)
        canvas.drawBitmap(reflectionImage,reflectionsrcRectF,reflectionrectF,paint);


        val  linearGradientPaint = Paint()
        linearGradientPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.DST_IN))
        linearGradientPaint.setShader(linearGradient)
        canvas.drawRect(0f,height - reflectionrectF.height(),reflectionrectF.width(),height.toFloat(),linearGradientPaint);

        paint.setXfermode(null)
        return meinv

    }

    private fun createRoundImage(canvas: Canvas,width: Int, height: Int): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val layer: Int = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), paint, Canvas.ALL_SAVE_FLAG)
        val target = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(target)
        val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())



        //绘制图片
        paint.color = Color.parseColor("#ffffff00");
        val roundrectF = RectF(80f, 80f, width.toFloat() - 80f, height.toFloat()- 80f)
        //具有形状的drawRoundRect必须在setXfermode调用之前添加，这样切出来的才是想要的形状
        canvas.drawRoundRect(roundrectF, 60f, 60f, paint)
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN)) //该模式可以让两个重叠,并取交集


        paint.color = Color.parseColor("#70ffff00");
        canvas.drawRect(0f,0f,width.toFloat(),height.toFloat(),paint)
        //绘制圆角矩形
        paint.color = Color.RED;
        canvas.drawRect(50f, 50f, width.toFloat(), height.toFloat(),paint)
        paint.setXfermode(null)
        canvas.restoreToCount(layer)

        return target
    }



}