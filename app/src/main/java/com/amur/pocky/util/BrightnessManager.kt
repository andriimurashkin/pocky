package com.amur.pocky.util

import android.app.Activity
import android.view.WindowManager

object BrightnessManager {
    fun setMaxBrightness(activity: Activity) {
        activity.window.attributes = activity.window.attributes.apply {
            screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL
        }
    }

    fun restoreBrightness(activity: Activity) {
        activity.window.attributes = activity.window.attributes.apply {
            screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
        }
    }
}
