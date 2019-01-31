package life.sabujak.roundedbutton

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

    //button
    private val buttonRectF = RectF()
    private val buttonPaint = Paint()

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

    private var rippleColor: Int
    private var rippleRadius = 0f
    private var rippleAlpha = 100

    private val mPath = Path()

    //shadow
    private val shadowRectF = RectF()
    private val shadowPaint = Paint()


    private var radiusProperty = object : Property<RoundedButton, Float>(Float::class.java, "radius") {
        override fun get(button: RoundedButton?): Float {
            return getRippleRadius()
        }

        override fun set(button: RoundedButton?, value: Float?) {
            setRippleRadius(value ?: 0f)
        }
    }


    private var circleAlphaProperty = object : Property<RoundedButton, Int>(Int::class.java, "rippleAlpha") {
        override fun get(button: RoundedButton?): Int? {
            return getRippleAlpha()
        }

        override fun set(button: RoundedButton?, value: Int?) {
            setRippleAlpha(value ?: 100)
        }
    }

    init {
        isClickable = true

        val styledAttrs = getContext().theme
            .obtainStyledAttributes(attrs, R.styleable.RoundedButton, 0, 0)

        buttonCornerRadius = styledAttrs.getDimension(R.styleable.RoundedButton_buttonCornerRadius, 0f)
        buttonBackgroundColor = styledAttrs
            .getColor(
                R.styleable.RoundedButton_buttonColor,
                ContextCompat.getColor(getContext(), R.color.colorDefault)
            )

        buttonGradientStartColor = styledAttrs
            .getColor(R.styleable.RoundedButton_buttonGradientStartColor, -1)

        buttonGradientEndColor = styledAttrs
            .getColor(R.styleable.RoundedButton_buttonGradientEndColor, -1)

        //ripple
        rippleColor = styledAttrs.getColor(
            R.styleable.RoundedButton_buttonRippleColor,
            Color.WHITE
        )
        rippleAlpha = styledAttrs.getInt(R.styleable.RoundedButton_buttonRippleAlpha, 100)

        initButtonPaint()
        initRipplePaint()
        initShadowPaint()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0
            && h > 0
            && buttonGradientStartColor != -1
            && buttonGradientEndColor != -1
        )
            setGradient(w.toFloat(), h.toFloat())
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        val offset = 4f
        var radius = buttonCornerRadius

        buttonRectF.set(offset, offset * 2, width.toFloat() - offset, height.toFloat() - offset * 2)
        shadowRectF.set(buttonRectF)

        shadowRectF.bottom += offset *2
        canvas.drawRoundRect(shadowRectF, radius, radius, shadowPaint)

        mPath.rewind()
        mPath.addRoundRect(
            buttonRectF, buttonCornerRadius,
            buttonCornerRadius, Path.Direction.CCW
        )
        canvas.clipPath(mPath)

        canvas.drawRoundRect(buttonRectF, radius, radius, buttonPaint)

        canvas.drawRoundRect(buttonRectF, radius, radius, buttonPaint)

        super.onDraw(canvas)

        //ripple
        canvas.drawCircle(currentCoords.x.toFloat(), currentCoords.y.toFloat(), getRippleRadius(), ripplePaint)
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


    private fun initButtonPaint() {
        buttonPaint.isAntiAlias = true
        buttonPaint.style = Paint.Style.FILL
        buttonPaint.color = buttonBackgroundColor
    }

    private fun initRipplePaint() {
        ripplePaint.isAntiAlias = true
        ripplePaint.color = rippleColor
        ripplePaint.alpha = rippleAlpha
    }

    private fun initShadowPaint() {
        shadowPaint.isAntiAlias = true
        shadowPaint.style = Paint.Style.FILL
        shadowPaint.color = Color.parseColor("#42000000")
    }

    private fun startRipple() {
        val endRadius = getEndRadius()

        cancelAllAnimations()

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

        setRippleRadius(getStartRadius())
        setRippleAlpha(100)

        val ripple = ObjectAnimator.ofFloat(this, radiusProperty, rippleRadius, endRadius)

        ripple.duration = 200
        ripple.interpolator = DecelerateInterpolator()
        hoverAnimator.play(ripple)
        hoverAnimator.start()
    }

    private fun getRippleRadius(): Float {
        return rippleRadius
    }

    private fun setRippleRadius(radius: Float) {
        this.rippleRadius = radius
        invalidate()
    }

    private fun getRippleAlpha(): Int {
        return ripplePaint.alpha
    }

    private fun setRippleAlpha(rippleAlpha: Int) {
        ripplePaint.alpha = rippleAlpha
        invalidate()
    }


    private fun getEndRadius() = getStartRadius() * 16

    private fun getStartRadius(): Float {
        val distance = (if (width > height) width else height) / 2f

        return distance / 32
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
        buttonPaint.shader = LinearGradient(
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