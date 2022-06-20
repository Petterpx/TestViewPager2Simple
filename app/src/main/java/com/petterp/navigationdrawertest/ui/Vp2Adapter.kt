package com.petterp.navigationdrawertest.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.petterp.navigationdrawertest.ItemFragment

/**
 *
 * @author petterp To 2022/6/13
 */
class Vp2Adapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return ItemFragment(position)
    }
}
