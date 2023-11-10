package com.example.rtjhapp.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.RectF
import android.graphics.SweepGradient
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.rtjhapp.R
import com.example.rtjhapp.utils.TypeUtils.Companion.isNum
import java.text.DecimalFormat

//圆形进度条
class CircleProgressView(context: Context?, attrs: AttributeSet?): View(context,attrs) {
    // 是否开启抗锯齿
    private var antiAlias: Boolean = true

    // 圆心位置
    private lateinit var centerPosition: Point

    // 半径
    private var radius: Float? = null

    // 声明边界矩形
    private var mRectF: RectF? = null

    // 声明背景圆画笔
    private lateinit var mBgCirPaint:Paint // 画笔
    private var mBgCirColor: Int? = null // 颜色
    private var mBgCirWidth: Float = 15f // 圆环背景宽度

    // 声明进度圆画笔
    private lateinit var mCirPaint: Paint // 画笔
    private var mCirColor: Int? = null // 颜色
    private  var mCirWidth: Float = 15f // 主圆的宽度

    // 绘制的起始角度和滑过角度（默认从顶部开始绘制，绘制360度）
    private var mStartAngle: Float = 270f
    private var mSweepAngle: Float = 360f

    // 动画时间
    private var mAnimTime: Int = 1000

    // 属性动画
    private var mAnimator: ValueAnimator? = null

    // 动画进度
    private var mAnimPercent: Float = 0f

    // 进度值
    private var mValue: String? = null

    // 最大值（默认100）
    private var mMaxValue: Float = 100f

    // 绘制数值
    private lateinit var mValuePaint: TextPaint
    private var mValueSize: Float? = null
    private var mValueColor: Int? = null

    // 绘制进度的后缀-默认为百分号
    private var mUnit: CharSequence? = R.string.celsius.toString()

    // 绘制描述
    private var mHint: CharSequence? = null
    private lateinit var mHintPaint: TextPaint
    private var mHintSize: Float? = null
    private var mHintColor: Int? = null

    // 颜色渐变
    private var isGradient: Boolean? = null
    private var mGradientColors: IntArray? = intArrayOf(Color.RED,Color.GRAY,Color.BLUE)
    private var mGradientColor: Int? = null
    private var mSweepGradient: SweepGradient? = null

    // 阴影
    private var mShadowColor: Int? = null
    private var mShadowSize: Float? = null
    private var mShadowIsShow: Boolean = false

    // 保留的小数位数（默认两位）
    private var mDigit: Int = 2

    // 是否需要动画（默认需要动画）
    private var isAnim: Boolean = true

    // 图标渲染
    private var mHintDrawable: Drawable? = null

    init {
        try {
            setLayerType(LAYER_TYPE_SOFTWARE,null)
            mAnimPercent = 0f
            centerPosition = Point()// 初始化圆心属性
            mRectF = RectF()
            mAnimator = ValueAnimator()// 初始化属性动画
            initAttrs(attrs, context)//初始化属性
            Log.d("CircleProgressView","Attempting to instantiate CircleProgressView")
            initPaint()//初始化画笔
        } catch (e:Exception) {
            // 捕捉任何异常
            Log.e("CircleProgressView", "Error during instantiation: ${e.localizedMessage}")
        }
    }
    /**
     * 初始化属性
     */
    private fun initAttrs(attrs:AttributeSet?,context: Context?){
        val typeArray = context!!.obtainStyledAttributes(attrs, R.styleable.CircleProgressView)
        isAnim = typeArray.getBoolean(R.styleable.CircleProgressView_isanim,true)
        mDigit = typeArray.getInt(R.styleable.CircleProgressView_digit,2)
        mBgCirColor = typeArray.getColor(R.styleable.CircleProgressView_mBgCirColor, Color.GRAY)
        mBgCirWidth = typeArray.getDimension(R.styleable.CircleProgressView_mBgCirWidth,15f)
        mCirColor = typeArray.getColor(R.styleable.CircleProgressView_mCirColor,Color.YELLOW)
        mCirWidth = typeArray.getDimension(R.styleable.CircleProgressView_mCirWidth,15f)
        mAnimTime = typeArray.getInt(R.styleable.CircleProgressView_animTime,1000)
        mValue = typeArray.getString(R.styleable.CircleProgressView_value)
        mMaxValue = typeArray.getFloat(R.styleable.CircleProgressView_maxvalue,100f)
        mStartAngle = typeArray.getFloat(R.styleable.CircleProgressView_startAngle,270f)
        mSweepAngle = typeArray.getFloat(R.styleable.CircleProgressView_sweepAngle,360f)
        mValueSize = typeArray.getDimension(R.styleable.CircleProgressView_valueSize,15f)
        mValueColor = typeArray.getColor(R.styleable.CircleProgressView_valueColor,Color.BLACK)
        mHintDrawable = typeArray.getDrawable(R.styleable.CircleProgressView_hintDrawable)
        mHint = typeArray.getString(R.styleable.CircleProgressView_hint)
        mHintSize = typeArray.getDimension(R.styleable.CircleProgressView_hintSize,15f)
        mHintColor = typeArray.getColor(R.styleable.CircleProgressView_hintColor,Color.GRAY)
        mUnit = typeArray.getString(R.styleable.CircleProgressView_unit)
        mShadowColor = typeArray.getColor(R.styleable.CircleProgressView_shadowColor,Color.BLACK)
        mShadowIsShow = typeArray.getBoolean(R.styleable.CircleProgressView_shadowShow,false)
        mShadowSize = typeArray.getFloat(R.styleable.CircleProgressView_shadowSize,8f)
        isGradient = typeArray.getBoolean(R.styleable.CircleProgressView_isGradient,false)
        mGradientColor = typeArray.getResourceId(R.styleable.CircleProgressView_gradient,0)
        if(mGradientColor != 0){
            mGradientColors = resources.getIntArray(mGradientColor!!)
        }
        typeArray.recycle()
    }

    /**
     * 初始化画笔
     */
    private fun initPaint(){
        // 圆画笔（主圆的画笔设置）
        mCirPaint = Paint()
        mCirPaint.isAntiAlias = antiAlias // 是否开启抗锯齿
        mCirPaint.style = Paint.Style.STROKE // 画笔样式
        mCirPaint.strokeWidth = mCirWidth // 画笔宽度
        mCirPaint.strokeCap = Paint.Cap.ROUND // 笔刷样式（圆角的效果）
        mCirPaint.color = mCirColor!!// 画笔颜色

        // 背景圆画笔（一般和主圆一样大或者小于主圆的宽度）
        mBgCirPaint = Paint()
        mBgCirPaint.isAntiAlias = antiAlias
        mBgCirPaint.style = Paint.Style.STROKE
        mBgCirPaint.strokeWidth = mBgCirWidth
        mBgCirPaint.strokeCap = Paint.Cap.ROUND
        mBgCirPaint.color = mBgCirColor!!

        // 初始化主题文字的字体画笔
        mValuePaint = TextPaint()
        mValuePaint.isAntiAlias = antiAlias
        mValuePaint.textSize = mValueSize!! // 字体大小
        mValuePaint.color = mValueColor!! // 字体颜色
        mValuePaint.textAlign = Paint.Align.CENTER // 从中间向两边绘制，不需要再次计算文字

        //初始化提示文本的字体画笔
        mHintPaint = TextPaint()
        mHintPaint.isAntiAlias = antiAlias
        mHintPaint.textSize = mHintSize!!
        mHintPaint.color = mHintColor!!
        mHintPaint.textAlign = Paint.Align.CENTER
        mHintPaint.typeface = Typeface.DEFAULT_BOLD
    }

    /**
     * 设置圆形和矩阵的大小，设置圆心位置
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 圆心位置
        centerPosition.x = w / 2
        centerPosition.y = h / 2

        // 半径
        val maxCirWidth = mCirWidth.coerceAtLeast(mBgCirWidth)
        val minWidth =
            (w - paddingLeft - paddingRight - 2 * maxCirWidth).coerceAtMost(h - paddingBottom - paddingTop - 2 * maxCirWidth)

        radius = minWidth / 2

        //矩阵坐标
        mRectF!!.left = centerPosition.x - radius!! - maxCirWidth / 2
        mRectF!!.top = centerPosition.y - radius!! - maxCirWidth / 2
        mRectF!!.right = centerPosition.x + radius!! + maxCirWidth / 2
        mRectF!!.bottom = centerPosition.y + radius!! + maxCirWidth / 2

        if (isGradient!!){
            setupGradientCircle() // 设置圆环画笔颜色渐变
        }
    }

    /**
     * 核心方法-绘制文本与圆环
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawIcon(canvas)
        drawCircle(canvas)
    }

    /**
     * 绘制中心的文本
     */
    private fun drawIcon(canvas: Canvas){
        mHintDrawable?.let {
            val drawableHeight = it.intrinsicHeight
            val baseline = (height - drawableHeight) / 2 - 20
            it.setBounds(
                centerPosition.x - it.intrinsicWidth / 2,
                baseline,
                centerPosition.x + it.intrinsicWidth / 2,
                (baseline + drawableHeight)
            )
            it.draw(canvas)
        }

        mValue?.let{
            val textBaseline = centerPosition.y + mValuePaint.textSize
            mValuePaint.isFakeBoldText = true
            canvas.drawText(
                it + mUnit,
                centerPosition.x.toFloat(),
                textBaseline,
                mValuePaint
            )
        }
    }

    /**
     * 使用渐变色画圆
     */
    private fun setupGradientCircle(){
        mSweepGradient = SweepGradient(
            centerPosition.x.toFloat(),
            centerPosition.y.toFloat(),
            mGradientColors!!,
            null
        )
        mCirPaint.shader = mSweepGradient
    }

    /**
     * 画圆（主要的圆）
     */
    private fun drawCircle(canvas: Canvas?){
        canvas?.save()
        if(mShadowIsShow){
            mCirPaint.setShadowLayer(mShadowSize!!,0f,0f,mShadowColor!!) // 设置阴影
        }

        // 画背景圆
        canvas?.drawArc(mRectF!!,mStartAngle,mSweepAngle,false,mBgCirPaint)

        // 画圆
        canvas?.drawArc(mRectF!!,mStartAngle,mSweepAngle * mAnimPercent, false,mCirPaint)
        canvas?.restore()
    }

    /**
     * 设置当前需要展示的值
     */
    fun setValue(value: String, maxValue: Float):CircleProgressView {
        if(isNum(value)){
            mValue = value
            mMaxValue = maxValue

            //当前的进度和最大的进度，去做动画的绘制
            val start = mAnimPercent
            val end = value.toFloat() / maxValue
            startAnim(start,end,mAnimTime)
        } else {
            mValue = value
        }
        return this
    }

    /**
     * 执行属性动画
     */
    private fun startAnim(start:Float,end:Float,animTime:Int){
        mAnimator = ValueAnimator.ofFloat(start,end)
        mAnimator?.duration = animTime.toLong()
        mAnimator?.addUpdateListener {
            //得到当前的动画进度并赋值
            mAnimPercent = it.animatedValue as Float

            // 根据当前的动画得到当前的值
            mValue = if (isAnim){
                CircleUtil.roundByScale((mAnimPercent * mMaxValue).toDouble(),mDigit)
            } else {
                CircleUtil.roundByScale(mValue!!.toDouble(),mDigit)
            }

            // 不停的重绘当前的值-表现出动画效果
            postInvalidate()
        }
        mAnimator?.start()
    }

    /**
     * 设置动画时长
     */
    fun setAnimTIme(animTime: Int):CircleProgressView {
        this.mAnimTime = animTime
        invalidate()
        return this
    }

    /**
     * 是否渐变色
     * */
    fun setIsGradient(isGradient: Boolean): CircleProgressView {
        this.isGradient = isGradient
        invalidate()
        return this
    }

    /**
     * 设置渐变色
     * */
    fun setGradientColors(gradientColors: IntArray): CircleProgressView {
        mGradientColors = gradientColors
        setupGradientCircle()
        return this
    }

    /**
     * 是否显示阴影
     * */
    fun setShadowEnable(enable: Boolean): CircleProgressView {
        mShadowIsShow = enable
        invalidate()
        return this
    }


    /**
    * 内部工具类
    */
    private class CircleUtil {

        companion object {
            /**
             * 将double格式化为指定小数位的String，不足小数位用0补全
             *
             * @param v     需要格式化的数字
             * @param scale 小数点后保留几位
             * @return
             */
            fun roundByScale(v: Double, scale: Int): String {
                if (scale < 0) {
                    throw IllegalArgumentException("参数错误，必须设置大于0的数字")
                }
                if (scale == 0) {
                    return DecimalFormat("0").format(v)
                }
                var formatStr = "0."

                for (i in 0 until scale) {
                    formatStr += "0"
                }
                return DecimalFormat(formatStr).format(v);
            }

            fun dip2px(context: Context, dpValue: Float): Int {
                val scale = context.resources.displayMetrics.density
                return (dpValue * scale + 0.5f).toInt()
            }

            fun dp2px(context: Context, dpValue: Float): Int {
                return dip2px(context, dpValue)
            }

            fun px2dip(context: Context, pxValue: Float): Int {
                val scale = context.resources.displayMetrics.density
                return (pxValue / scale + 0.5f).toInt()
            }

            fun px2sp(context: Context, pxValue: Float): Int {
                val fontScale = context.resources.displayMetrics.scaledDensity
                return (pxValue / fontScale + 0.5f).toInt()
            }

            fun sp2px(context: Context, spValue: Float): Int {
                val fontScale = context.resources.displayMetrics.scaledDensity
                return (spValue * fontScale + 0.5f).toInt()
            }

        }
    }
}