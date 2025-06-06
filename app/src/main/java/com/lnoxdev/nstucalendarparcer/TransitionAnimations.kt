package com.lnoxdev.nstucalendarparcer

import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.transition.Fade

object TransitionAnimations {
    private const val TRANS_ANIM_DURATION = 200L

    fun initFadeAnimation(fragment: Fragment) {
        fragment.enterTransition = getFadeTransition()
        fragment.exitTransition = getFadeTransition()
        fragment.returnTransition = getFadeTransition()
        fragment.reenterTransition = getFadeTransition()
    }

    private fun getFadeTransition(): Fade {
        val fade = Fade()
        fade.duration = TRANS_ANIM_DURATION
        fade.interpolator = LinearOutSlowInInterpolator()
        return fade
    }
}