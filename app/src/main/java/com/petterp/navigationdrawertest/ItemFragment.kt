package com.petterp.navigationdrawertest

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.petterp.navigationdrawertest.ui.MainAdapter
import com.petterp.navigationdrawertest.ui.ViewPagerLayoutManager

/**
 *
 * @author petterp To 2022/6/13
 */
class ItemFragment(val position: Int) : Fragment(R.layout.item_fragment) {
    lateinit var rv: RecyclerView
    lateinit var tvPosition: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv = view.findViewById(R.id.ivMain)
        initRv()
    }

    private fun initRv() {
        val list = listOf(
            "$position-1",
            "$position-2",
            "$position-3",

        )
        val adapter = MainAdapter(list)
        val viewPagerLayoutManager = ViewPagerLayoutManager(requireContext(), RecyclerView.VERTICAL)
        rv.layoutManager = viewPagerLayoutManager
        rv.adapter = adapter
    }
}
