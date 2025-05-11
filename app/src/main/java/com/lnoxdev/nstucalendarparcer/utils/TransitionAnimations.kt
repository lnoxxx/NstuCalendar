package com.lnoxdev.nstucalendarparcer.utils

import android.view.Gravity
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.transition.Fade
import androidx.transition.Slide

object TransitionAnimations {
    private const val TRANS_ANIM_DURATION = 300L
    private const val FADE_DURATION = 200L

    fun initDefaultEnterTransitionAnimation(fragment: Fragment) {
        fragment.enterTransition = getDefaultEnterTransition()
    }

    fun initDefaultExitTransitionAnimation(fragment: Fragment) {
        fragment.exitTransition = getDefaultExitTransition()
    }

    private fun getDefaultEnterTransition(): Slide {
        val slideEnd = Slide(Gravity.END)
        slideEnd.duration = TRANS_ANIM_DURATION
        slideEnd.interpolator = LinearOutSlowInInterpolator()
        return slideEnd
    }

    private fun getDefaultExitTransition(): Fade {
        val fade = Fade()
        fade.duration = FADE_DURATION
        fade.interpolator = LinearOutSlowInInterpolator()
        return fade
    }
}