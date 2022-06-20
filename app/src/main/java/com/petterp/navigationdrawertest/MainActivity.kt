package com.petterp.navigationdrawertest

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.customview.widget.ViewDragHelper
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commitNow
import androidx.viewpager2.widget.ViewPager2
import com.petterp.navigationdrawertest.databinding.ActivityMainBinding
import com.petterp.navigationdrawertest.ui.Vp2Adapter
import java.lang.reflect.Field
import kotlin.math.max

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val fragment = WangFragment()

    private var pos = 0
    private var prePos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.commitNow {
            replace(R.id.flNav, fragment)
        }
        binding.flNav.layoutParams.apply {
            width = screenWidth
        }
        binding.drawerLayout.setScrimColor(Color.TRANSPARENT)
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                binding.page.left = -(binding.drawerLayout.right * slideOffset).toInt()
            }

            override fun onDrawerOpened(drawerView: View) {
            }

            override fun onDrawerClosed(drawerView: View) {
            }

            override fun onDrawerStateChanged(newState: Int) {
            }
        })
        binding.drawerLayout.postDelayed({
            setDrawerLeftEdgeSize(binding.drawerLayout, false)
        }, 100)
        binding.page.adapter = Vp2Adapter(this)
        binding.page.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                prePos = pos
                pos = position
            }
        })
    }

    val Context.screenWidth: Int
        get() {
            val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val dm = DisplayMetrics()
            display.getMetrics(dm)
            return dm.widthPixels
        }

    fun setDrawerLeftEdgeSize(
        drawerLayout: DrawerLayout,
        isLeft: Boolean = true,
        displayWidthPercentage: Float = 1f
    ) {
        try {
            val (viewDragHelper, viewDragCallback) = if (isLeft) {
                "mLeftDragger" to "mLeftCallback"
            } else "mRightDragger" to "mRightCallback"
            val viewDragField: Field = drawerLayout.javaClass.getDeclaredField(viewDragHelper)
            viewDragField.isAccessible = true
            val dragHelper = viewDragField.get(drawerLayout) as ViewDragHelper
            val edgeSizeField: Field = dragHelper.javaClass.getDeclaredField("mEdgeSize")
            edgeSizeField.isAccessible = true
            val edgeSize = edgeSizeField.getInt(dragHelper)
            edgeSizeField.setInt(
                dragHelper,
                max(edgeSize, (screenWidth * displayWidthPercentage).toInt())
            )
            val callbackField: Field = drawerLayout.javaClass.getDeclaredField(viewDragCallback)
            callbackField.isAccessible = true
            // 因为无法直接访问私有内部类，所以该私有内部类实现的接口非常重要，通过多态的方式获取实例
            val callback = callbackField[drawerLayout] as ViewDragHelper.Callback
            val peekRunnableField = callback.javaClass.getDeclaredField("mPeekRunnable")
            peekRunnableField.isAccessible = true
            peekRunnableField[callback] = Runnable {
                Log.e("petterp", "什么时候调用")
            }
        } catch (e: Exception) {
            Log.e("petterp", e.localizedMessage)
        }
    }
}

class WangFragment() : Fragment() {

    private lateinit var tvMain: TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return TextView(context).apply {
            setBackgroundColor(Color.RED)
            layoutParams = ViewGroup.LayoutParams(-1, -1)
            tvMain = this
            textSize = 30f
            gravity = Gravity.CENTER
        }
    }

    fun setText(position: Int) {
        tvMain.text = position.toString()
    }

    override fun onResume() {
        super.onResume()
        Log.e("petterp", "onResume")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("petterp", "onViewCreated")
    }
}
