package com.tapadoo.alerter

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.view.ContextThemeWrapper
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.widget.TextViewCompat
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.alerter_alert_view.view.*

/**
 * Custom Alert View
 *
 * @author Kevin Murphy, Tapadoo, Dublin, Ireland, Europe, Earth.
 * @since 26/01/2016
 */
class Alert @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0)
    : FrameLayout(context, attrs, defStyle), View.OnClickListener, Animation.AnimationListener, SwipeDismissTouchListener.DismissCallbacks {

    private var onShowListener: OnShowAlertListener? = null
    internal var onHideListener: OnHideAlertListener? = null

    internal var enterAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.alerter_slide_in_from_top)
    internal var exitAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.alerter_slide_out_to_top)

    internal var duration = DISPLAY_TIME_IN_SECONDS

    private var showIcon: Boolean = true
    private var enableIconPulse = true
    private var enableInfiniteDuration: Boolean = false
    private var enableProgress: Boolean = false

    private var runningAnimation: Runnable? = null

    private var isDismissable = true

    private var buttons = ArrayList<Button>()
    var buttonTypeFace: Typeface? = null

    /**
     * Flag to ensure we only set the margins once
     */
    private var marginSet: Boolean = false

    /**
     * Flag to enable / disable haptic feedback
     */
    private var vibrationEnabled = true

    /**
     * Sets the Gravity of the Alert
     *
     * @param contentGravity Gravity of the Alert
     */
    var contentGravity: Int
        get() = (llAlertBackground?.layoutParams as FrameLayout.LayoutParams).gravity
        set(contentGravity) {
            val paramsTitle = tvTitle?.layoutParams as? LinearLayout.LayoutParams
            paramsTitle?.gravity = contentGravity
            tvTitle?.layoutParams = paramsTitle

            val paramsText = tvText?.layoutParams as? LinearLayout.LayoutParams
            paramsText?.gravity = contentGravity
            tvText?.layoutParams = paramsText
        }

    init {
        inflate(context, R.layout.alerter_alert_view, this)
        isHapticFeedbackEnabled = true

        ViewCompat.setTranslationZ(this, Integer.MAX_VALUE.toFloat())

        llAlertBackground.setOnClickListener(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        enterAnimation.setAnimationListener(this)

        // Set Animation to be Run when View is added to Window
        animation = enterAnimation

        // Add all buttons
        buttons.forEach { button ->
            buttonTypeFace?.let { button.typeface = it }
            llButtonContainer.addView(button)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (!marginSet) {
            marginSet = true

            // Add a negative top margin to compensate for overshoot enter animation
            val params = layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = context.resources.getDimensionPixelSize(R.dimen.alerter_alert_negative_margin_top)

            // Check for Cutout
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val displayCutout = (context as? Activity)?.window?.decorView?.rootWindowInsets?.displayCutout

                val notchHeight = displayCutout?.safeInsetTop ?: 0
                llAlertBackground.apply {
                    setPadding(paddingLeft, paddingTop + (notchHeight / 2), paddingRight, paddingBottom)
                }
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    // Release resources once view is detached.
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        enterAnimation.setAnimationListener(null)
    }

    /* Override Methods */

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.performClick()
        return super.onTouchEvent(event)
    }

    override fun onClick(v: View) {
        if (isDismissable) {
            hide()
        }
    }

    override fun setOnClickListener(listener: View.OnClickListener?) {
        llAlertBackground.setOnClickListener(listener)
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        for (i in 0 until childCount) {
            getChildAt(i).visibility = visibility
        }
    }

    /* Interface Method Implementations */

    override fun onAnimationStart(animation: Animation) {
        if (!isInEditMode) {
            visibility = View.VISIBLE

            if (vibrationEnabled) {
                performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            }

            if (enableProgress) {
                ivIcon?.visibility = View.INVISIBLE
                pbProgress?.visibility = View.VISIBLE
            } else if (showIcon) {
                ivIcon?.visibility = View.VISIBLE
                // Only pulse if we're not showing the progress
                if (enableIconPulse) {
                    ivIcon?.startAnimation(AnimationUtils.loadAnimation(context, R.anim.alerter_pulse))
                }
            } else {
                flIconContainer.visibility = View.GONE
            }
        }
    }

    override fun onAnimationEnd(animation: Animation) {
        onShowListener?.onShow()

        startHideAnimation()
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private fun startHideAnimation() {
        //Start the Handler to clean up the Alert
        if (!enableInfiniteDuration) {
            runningAnimation = Runnable { hide() }

            postDelayed(runningAnimation, duration)
        }
    }

    override fun onAnimationRepeat(animation: Animation) {
        //Ignore
    }

    /* Clean Up Methods */

    /**
     * Cleans up the currently showing alert view.
     */
    private fun hide() {
        try {
            exitAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    llAlertBackground?.setOnClickListener(null)
                    llAlertBackground?.isClickable = false
                }

                override fun onAnimationEnd(animation: Animation) {
                    removeFromParent()
                }

                override fun onAnimationRepeat(animation: Animation) {
                    //Ignore
                }
            })

            startAnimation(exitAnimation)
        } catch (ex: Exception) {
        }
    }

    /**
     * Removes Alert View from its Parent Layout
     */
    internal fun removeFromParent() {
        clearAnimation()
        visibility = View.GONE

        postDelayed(object : Runnable {
            override fun run() {
                try {
                    if (parent != null) {
                        try {
                            (parent as ViewGroup).removeView(this@Alert)

                            onHideListener?.onHide()
                        } catch (ex: Exception) {
                        }
                    }
                } catch (ex: Exception) {
                }
            }
        }, CLEAN_UP_DELAY_MILLIS.toLong())
    }

    /* Setters and Getters */

    /**
     * Sets the Alert Background colour
     *
     * @param color The qualified colour integer
     */
    fun setAlertBackgroundColor( dark: Boolean) {
        if(dark){
            llAlertBackground.setBackgroundResource(R.drawable.shap_b)
            tvTitle.setTextColor(Color.WHITE)
            tvText.setTextColor(Color.WHITE)
        }
        else {
            llAlertBackground.setBackgroundResource(R.drawable.shap_w)
            tvTitle.setTextColor(Color.BLACK)
            tvText.setTextColor(Color.BLACK)

        }
    }

    /**
     * Sets the Alert Background Drawable Resource
     *
     * @param resource The qualified drawable integer
     */
    fun setAlertBackgroundResource(@DrawableRes resource: Int) {
        llAlertBackground.setBackgroundResource(resource)
    }

    /**
     * Sets the Alert Background Drawable
     *
     * @param drawable The qualified drawable
     */
    fun setAlertBackgroundDrawable(drawable: Drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            llAlertBackground.background = drawable
        } else {
            llAlertBackground.setBackground(drawable)
        }
    }

    /**
     * Sets the Title of the Alert
     *
     * @param titleId String resource id of the Alert title
     */
    fun setTitle(@StringRes titleId: Int) {
        setTitle(context.getString(titleId))
    }

    /**
     * Sets the Text of the Alert
     *
     * @param textId String resource id of the Alert text
     */
    fun setText(@StringRes textId: Int) {
        setText(context.getString(textId))
    }

    /**
     * Disable touches while the Alert is showing
     */
    fun disableOutsideTouch() {
        flClickShield.isClickable = true
    }

    /**
     * Sets the Title of the Alert
     *
     * @param title CharSequence object to be used as the Alert title
     */
    fun setTitle(title: CharSequence) {
        if (!TextUtils.isEmpty(title)) {
            tvTitle.visibility = View.VISIBLE
            tvTitle.text = title
        }
    }

    /**
     * Set the Title's text appearance of the Title
     *
     * @param textAppearance The style resource id
     */
    fun setTitleAppearance(@StyleRes textAppearance: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tvTitle.setTextAppearance(textAppearance)
        } else {
            TextViewCompat.setTextAppearance(tvText, textAppearance)
        }
    }

    /**
     * Set the Title's typeface
     *
     * @param typeface The typeface to use
     */
    fun setTitleTypeface(typeface: Typeface) {
        tvTitle.typeface = typeface
    }

    /**
     * Set the Text's typeface
     *
     * @param typeface The typeface to use
     */
    fun setTextTypeface(typeface: Typeface) {
        tvText.typeface = typeface
    }

    /**
     * Sets the Text of the Alert
     *
     * @param text CharSequence object to be used as the Alert text
     */
    fun setText(text: CharSequence) {
        if (!TextUtils.isEmpty(text)) {
            tvText.visibility = View.VISIBLE
            tvText.text = text
        }
    }

    /**
     * Set the Text's text appearance of the Title
     *
     * @param textAppearance The style resource id
     */
    fun setTextAppearance(@StyleRes textAppearance: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tvText.setTextAppearance(textAppearance)
        } else {
            TextViewCompat.setTextAppearance(tvText, textAppearance)
        }
    }

    /**
     * Set the inline icon for the Alert
     *
     * @param iconId Drawable resource id of the icon to use in the Alert
     */
    fun setIcon(ava:String) {
        if (ava == "no") {
            Picasso.get()
                    .load(R.drawable.profile)
                    .placeholder(R.drawable.placeholder_gray) .error(R.drawable.errorimg)
                    .into(ivIcon)
        } else {
            Picasso.get()
                    .load(ava)
                    .placeholder(R.drawable.placeholder_gray) .error(R.drawable.errorimg)
                    .into(ivIcon)
        }
    }

    /**
     * Set the icon color for the Alert
     *
     * @param color Color int
     */
    fun setIconColorFilter(@ColorInt color: Int) {
        ivIcon.setColorFilter(color)
    }

    /**
     * Set the icon color for the Alert
     *
     * @param colorFilter ColorFilter
     */
    fun setIconColorFilter(colorFilter: ColorFilter) {
        ivIcon.colorFilter = colorFilter
    }

    /**
     * Set the icon color for the Alert
     *
     * @param color Color int
     * @param mode  PorterDuff.Mode
     */
    fun setIconColorFilter(@ColorInt color: Int, mode: PorterDuff.Mode) {
        ivIcon.setColorFilter(color, mode)
    }

    /**
     * Set the inline icon for the Alert
     *
     * @param bitmap Bitmap image of the icon to use in the Alert.
     */
    fun setIcon(bitmap: Bitmap) {
        ivIcon.setImageBitmap(bitmap)
    }

    /**
     * Set the inline icon for the Alert
     *
     * @param drawable Drawable image of the icon to use in the Alert.
     */
    fun setIcon(drawable: Drawable) {
        ivIcon.setImageDrawable(drawable)
    }

    /**
     * Set whether to show the icon in the alert or not
     *
     * @param showIcon True to show the icon, false otherwise
     */
    fun showIcon(showIcon: Boolean) {
        this.showIcon = showIcon
    }

    /**
     * Set if the alerter is isDismissable or not
     *
     * @param dismissible True if alert can be dismissed
     */
    fun setDismissible(dismissible: Boolean) {
        this.isDismissable = dismissible
    }

    /**
     * Get if the alert is isDismissable
     * @return
     */
    fun isDismissable(): Boolean {
        return isDismissable
    }

    /**
     * Set whether to enable swipe to dismiss or not
     */
    fun enableSwipeToDismiss() {
        llAlertBackground.let {
            it.setOnTouchListener(SwipeDismissTouchListener(it, object : SwipeDismissTouchListener.DismissCallbacks {
                override fun canDismiss(): Boolean {
                    return true
                }

                override fun onDismiss(view: View) {
                    removeFromParent()
                }

                override fun onTouch(view: View, touch: Boolean) {
                    // Ignore
                }
            }))
        }
    }

    /**
     * Set if the Icon should pulse or not
     *
     * @param shouldPulse True if the icon should be animated
     */
    fun pulseIcon(shouldPulse: Boolean) {
        this.enableIconPulse = shouldPulse
    }

    /**
     * Set if the duration of the alert is infinite
     *
     * @param enableInfiniteDuration True if the duration of the alert is infinite
     */
    fun setEnableInfiniteDuration(enableInfiniteDuration: Boolean) {
        this.enableInfiniteDuration = enableInfiniteDuration
    }

    /**
     * Enable or disable progress bar
     *
     * @param enableProgress True to enable, False to disable
     */
    fun setEnableProgress(enableProgress: Boolean) {
        this.enableProgress = enableProgress
    }

    /**
     * Set the Progress bar color from a color resource
     *
     * @param color The color resource
     */
    fun setProgressColorRes(@ColorRes color: Int) {
        pbProgress?.progressDrawable?.colorFilter = LightingColorFilter(MUL, ContextCompat.getColor(context, color))
    }

    /**
     * Set the Progress bar color from a color resource
     *
     * @param color The color resource
     */
    fun setProgressColorInt(@ColorInt color: Int) {
        pbProgress?.progressDrawable?.colorFilter = LightingColorFilter(MUL, color)
    }

    /**
     * Set the alert's listener to be fired on the alert being fully shown
     *
     * @param listener Listener to be fired
     */
    fun setOnShowListener(listener: OnShowAlertListener) {
        this.onShowListener = listener
    }

    /**
     * Enable or Disable haptic feedback
     *
     * @param vibrationEnabled True to enable, false to disable
     */
    fun setVibrationEnabled(vibrationEnabled: Boolean) {
        this.vibrationEnabled = vibrationEnabled
    }

    /**
     * Show a button with the given text, and on click listener
     *
     * @param text The text to display on the button
     * @param onClick The on click listener
     */
    fun addButton(text: CharSequence, @StyleRes style: Int, onClick: View.OnClickListener) {
        Button(ContextThemeWrapper(context, style), null, style).apply {
            this.text = text
            this.setOnClickListener(onClick)

            buttons.add(this)
        }

        // Alter padding
        llAlertBackground?.apply {
            this.setPadding(this.paddingLeft, this.paddingTop, this.paddingRight, this.paddingBottom / 2)
        }
    }

    override fun canDismiss(): Boolean {
        return isDismissable
    }

    override fun onDismiss(view: View) {
        flClickShield?.removeView(llAlertBackground)
    }

    override fun onTouch(view: View, touch: Boolean) {
        if (touch) {
            removeCallbacks(runningAnimation)
        } else {
            startHideAnimation()
        }
    }

    companion object {

        private const val CLEAN_UP_DELAY_MILLIS = 100

        /**
         * The amount of time the alert will be visible on screen in seconds
         */
        private val DISPLAY_TIME_IN_SECONDS: Long = 3000
        private const val MUL = -0x1000000
    }
}
