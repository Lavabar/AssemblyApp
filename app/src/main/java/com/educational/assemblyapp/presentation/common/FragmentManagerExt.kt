package com.educational.assemblyapp.presentation.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.educational.assemblyapp.R.anim
import com.educational.assemblyapp.R.id

fun FragmentManager.navigate(fragment: Fragment) {
    commit(allowStateLoss = true) {
        setCustomAnimations(
            anim.slide_from_right,
            anim.slide_to_left,
            anim.slide_from_left,
            anim.slide_to_right
        )
        replace(id.main_activity_container, fragment)
        addToBackStack(null)
    }
}