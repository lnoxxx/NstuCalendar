package com.lnoxdev.nstucalendarparcer.utils

import android.view.Gravity
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.TransitionSet

object TransitionAnimations {
    private const val TRANS_ANIM_DURATION = 300L

    fun initDefaultTransitionAnimation(fragment: Fragment) {
        fragment.enterTransition = getDefaultEnterTransition()
        fragment.exitTransition = getDefaultExitTransition()
    }

    private fun getDefaultEnterTransition(): TransitionSet {
        val slideEnd = Slide(Gravity.END)
        slideEnd.duration = TRANS_ANIM_DURATION
        slideEnd.interpolator = LinearOutSlowInInterpolator()
        val fade = Fade()
        fade.duration = TRANS_ANIM_DURATION
        fade.interpolator = LinearOutSlowInInterpolator()
        val exitAnim = TransitionSet()
        exitAnim.addTransition(slideEnd)
        exitAnim.addTransition(fade)
        return exitAnim
    }

    private fun getDefaultExitTransition(): TransitionSet {
        val slideStart = Slide(Gravity.START)
        slideStart.duration = TRANS_ANIM_DURATION
        slideStart.interpolator = LinearOutSlowInInterpolator()
        val fade = Fade()
        fade.duration = TRANS_ANIM_DURATION
        fade.interpolator = LinearOutSlowInInterpolator()
        val enterAnim = TransitionSet()
        enterAnim.addTransition(slideStart)
        enterAnim.addTransition(fade)
        return enterAnim
    }
}