package com.example.character.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.character.R
import com.example.character.app.Config
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tool_bar.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        //tool bar
        tool_bar.title = Config.TITLE
        tool_bar.setBackgroundColor(resources.getColor(R.color.theme_yellow))

        adapter = ViewPagerAdapter(supportFragmentManager)
        view_pager.adapter = adapter
        adapter.setData()
        tab_layout.setupWithViewPager(view_pager)
    }
}