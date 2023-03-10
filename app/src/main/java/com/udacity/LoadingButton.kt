package com.udacity

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import kotlinx.android.synthetic.main.content_main.view.*
import kotlin.math.min
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private val valueAnimator = ValueAnimator()
    private var backgroundColour = 0
    private var foregroundColor = 0
    private var circleColour = 0
    private var progressColour = 0
    private var textColor = 0
    private var progress = 0
    private var buttonText= "Click Me"

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Clicked -> {
                buttonText = "Downloading..."
                animateButton()
            }

            ButtonState.Completed -> {
                buttonText = "Complete"
                valueAnimator.cancel()
            }
        }
        invalidate()
    }


    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
        color = ResourcesCompat.getColor(resources, R.color.colorAccent, null)
    }


    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            backgroundColour = getColor(R.styleable.LoadingButton_backgroundColor, 0)
            foregroundColor = getColor(R.styleable.LoadingButton_foregroundColor, 0)
            progressColour = getColor(R.styleable.LoadingButton_progressColor, 0)
            textColor = getColor(R.styleable.LoadingButton_textColor, 0)
            circleColour = getColor(R.styleable.LoadingButton_circleColor, 0)

        }

    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        widthSize = w
        heightSize = h

//        circle.set(
//            widthSize - 100f,
//            heightSize / 2 - 50f,
//            widthSize - 50f,
//            heightSize / 2 + 50f
//        )


    }

    // animate the button
    private fun animateButton() {
        valueAnimator.apply {
            setValues(PropertyValuesHolder.ofInt("progress", 0, 500))
            duration = 2000
            interpolator = LinearInterpolator()
            addUpdateListener {
                progress = it.getAnimatedValue("progress") as Int
                invalidate()
            }
            start()
        }

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.color = backgroundColour
        canvas?.drawRect(0f,0f,widthSize.toFloat(), heightSize.toFloat(), paint)

        // loading button
        paint.color = progressColour
        canvas?.drawRect(0f, 0f, widthSize * progress/360f, heightSize.toFloat(), paint)
        paint.color = textColor
        canvas?.drawText(
            buttonText,
            widthSize / 2f,
            heightSize / 2f + (paint.descent() - paint.ascent()) / 2 - paint.descent(),
            paint
        )
        paint.color = circleColour
//        canvas?.drawArc(circle, rotate, 90f, true, paint)
        canvas?.drawArc(widthSize - 100f,20f,widthSize - 50f,80f,0f, progress.toFloat(), true, paint)

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    fun setButtonStatus(buttonState: ButtonState) {
        (context as Activity).runOnUiThread {
            this.buttonState = buttonState
        }
    }
//    private fun animateButton() {
//        valueAnimator.apply {
//            setValues(
//                PropertyValuesHolder.ofFloat("circle", 0f, 360f),
//                PropertyValuesHolder.ofFloat("button", 0f, 1f)
//            )
//            duration = 1000
//            interpolator = LinearInterpolator()
//            repeatCount = ValueAnimator.INFINITE
//            repeatMode = ValueAnimator.RESTART
//            addUpdateListener {
//                rotate = it.getAnimatedValue("circle") as Float
//                invalidate()
//            }
//            start()
//        }
//    }
}