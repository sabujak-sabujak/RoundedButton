package life.sabujak.roundedbutton

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.util.Property
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator


class RoundedButton(context: Context?, attrs: AttributeSet?) : AppCompatTextView(context, attrs) {

    //attrs
    private val rectF = RectF()
    private val paint = Paint()

    private val buttonCornerRadius: Float
    private val buttonBackgroundColor: Int

    private val buttonGradientStartColor: Int
    private val buttonGradientEndColor: Int

    //ripple
    private val currentCoords = Point()
    private val previousCoords = Point()
    private val ripplePaint = Paint()
    private val rippleAnimator: AnimatorSet = AnimatorSet()
    private val fadeAnimator: AnimatorSet = AnimatorSet()
    private val hoverAnimator: AnimatorSet = AnimatorSet()

    private var rippleRadius = 0f
    private var rippleAlpha = 100

    private var radiusProperty = object : Property<RoundedButton, Float>(Float::class.java, "radius") {
        override fun get(button: RoundedButton?): Float {
            return getRadius()
        }

        override fun set(button: RoundedButton?, value: Float?) {
            setRadius(value ?: 0f)
        }
    }

    init {
        isClickable = true

        val styledAttrs = getContext().theme
                .obtainStyledAttributes(attrs, R.styleable.RoundedButton, 0, 0)

        buttonCornerRadius = styledAttrs.getDimension(R.styleable.RoundedButton_buttonCornerRadius, 0f)
        buttonBackgroundColor = styledAttrs
                .getColor(R.styleable.RoundedButton_buttonColor,
                        ContextCompat.getColor(getContext(), R.color.colorDefault))

        buttonGradientStartColor = styledAttrs
                .getColor(R.styleable.RoundedButton_buttonGradientStartColor, -1)

        buttonGradientEndColor = styledAttrs
                .getColor(R.styleable.RoundedButton_buttonGradientEndColor, -1)

        setPaint()

        //ripple
        ripplePaint.color = Color.parseColor("#ffffff")
        ripplePaint.alpha = rippleAlpha
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0
                && h > 0
                && buttonGradientStartColor != -1
                && buttonGradientEndColor != -1)
            setGradient(w.toFloat(), h.toFloat())
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        val offset = 0f
        val radius = buttonCornerRadius

        rectF.set(offset, offset, width.toFloat() - offset, height.toFloat() - offset)
        canvas.drawRoundRect(rectF, radius, radius, paint)
        super.onDraw(canvas)


        //ripple
        canvas.drawCircle(currentCoords.x.toFloat(), currentCoords.y.toFloat(), getRadius(), ripplePaint)
    }

    private fun setPaint() {
        paint.style = Paint.Style.FILL
        paint.color = buttonBackgroundColor
        paint.isAntiAlias = true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return super.onTouchEvent(event)
        when (event.action) {
            ACTION_DOWN -> {
                previousCoords.set(currentCoords.x, currentCoords.y)
                currentCoords.set(event.x.toInt(), event.y.toInt())
                startHover()
            }
            ACTION_UP -> {
                val bounds = RectF(0f, 0f, width.toFloat(), height.toFloat())
                val isEventInBounds = bounds.contains((event.x.toInt()).toFloat(), (event.y.toInt()).toFloat())
                if (ripplePaint.alpha != 0 && isEventInBounds) startRipple()
            }
            ACTION_MOVE -> {
                val bounds = RectF(0f, 0f, width.toFloat(), height.toFloat())
                val isEventInBounds = bounds.contains((event.x.toInt()).toFloat(), (event.y.toInt()).toFloat())
                if (!isEventInBounds && ripplePaint.alpha != 0) fade()
            }
        }
        return super.onTouchEvent(event)
    }

    private fun startRipple() {
        val endRadius = getEndRadius()

        cancelAllAnimations()

        rippleAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
            }
        })

        val ripple = ObjectAnimator.ofFloat(this, radiusProperty, rippleRadius, endRadius)
        ripple.duration = 200
        ripple.interpolator = DecelerateInterpolator()

        val fade = getFadeRippleObjectAnimator(ripple.duration)

        rippleAnimator.playTogether(ripple, fade)
        rippleAnimator.start()
    }

    private fun startHover() {
        val endRadius = getEndRadius()
        cancelAllAnimations()

        setRadius(0f)
        setRippleAlpha(100)

        val ripple = ObjectAnimator.ofFloat(this, radiusProperty, rippleRadius, endRadius)

        ripple.duration = 200
        ripple.interpolator = DecelerateInterpolator()
        hoverAnimator.play(ripple)
        hoverAnimator.start()
    }

    private fun getRadius(): Float {
        return rippleRadius
    }

    fun setRadius(radius: Float) {
        this.rippleRadius = radius
        invalidate()
    }

    private var circleAlphaProperty = object : Property<RoundedButton, Int>(Int::class.java, "rippleAlpha") {
        override fun get(`object`: RoundedButton): Int? {
            return `object`.getRippleAlpha()
        }

        override fun set(`object`: RoundedButton, value: Int?) {
            `object`.setRippleAlpha(value!!)
        }
    }

    fun getRippleAlpha(): Int {
        return ripplePaint.alpha
    }

    fun setRippleAlpha(rippleAlpha: Int) {
        ripplePaint.alpha = rippleAlpha
        invalidate()
    }


    private fun getEndRadius(): Float {
        val width = width
        val height = height

        val halfWidth = width / 2
        val halfHeight = height / 2

        val radiusX = if (halfWidth > currentCoords.x) width - currentCoords.x else currentCoords.x
        val radiusY = if (halfHeight > currentCoords.y) height - currentCoords.y else currentCoords.y

        return Math.sqrt(Math.pow(radiusX.toDouble(), 2.0) + Math.pow(radiusY.toDouble(), 2.0)).toFloat() * 1.2f
    }

    private fun fade() {
        fadeAnimator.play(getFadeRippleObjectAnimator())
        fadeAnimator.start()
    }

    private fun getFadeRippleObjectAnimator(delay: Long = 0) =
            ObjectAnimator.ofInt(this, circleAlphaProperty, rippleAlpha, 0)
                    .apply {
                        duration = 75
                        interpolator = AccelerateInterpolator()
                        startDelay = delay
                    }


    private fun setGradient(width: Float, height: Float) {
        paint.shader = LinearGradient(
                0f,
                0f,
                width,
                height,
                buttonGradientStartColor,
                buttonGradientEndColor,
                Shader.TileMode.REPEAT
        )
        invalidate()
    }

    private fun cancelAllAnimations() {
        rippleAnimator.cancel()
        rippleAnimator.removeAllListeners()

        hoverAnimator.cancel()

        fadeAnimator.cancel()
    }
}