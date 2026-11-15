package com.example.threadhandlerwizard

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private val vm: WorkViewModel by viewModels()
    private var player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val pager = findViewById<ViewPager2>(R.id.pager)
        val tabs = findViewById<TabLayout>(R.id.tabs)
        pager.offscreenPageLimit = 2 // 三頁都保持建立，切頁時狀態不會重來
        pager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 3
            override fun createFragment(position: Int): Fragment = when (position) {
                0 -> StartFragment()
                1 -> ProgressFragment()
                else -> ResultFragment()
            }
        }
        TabLayoutMediator(tabs, pager) { tab, pos ->
            tab.text = when (pos) {
                0 -> getString(R.string.tab_start)
                1 -> getString(R.string.tab_progress)
                else -> getString(R.string.tab_result)
            }
        }.attach()

        // 背景音樂：工作中播放，結束或中斷時停止
        vm.status.observe(this) { s ->
            when (s) {
                WorkViewModel.WORKING -> startMusic()
                WorkViewModel.DONE, WorkViewModel.CANCELED -> stopMusic()
            }
        }
    }

    private fun startMusic() {
        if (player != null) return
        player = MediaPlayer.create(this, R.raw.music)?.apply {
            isLooping = true
            start()
        }
    }

    private fun stopMusic() {
        player?.release()
        player = null
    }

    override fun onDestroy() {
        stopMusic()
        super.onDestroy()
    }
}
