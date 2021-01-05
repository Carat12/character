package com.example.character.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.character.app.Config

class ViewPagerAdapter(supportManage: FragmentManager): FragmentPagerAdapter(supportManage) {

    private var fragmentList: ArrayList<Fragment> = ArrayList()
    private var titleList: ArrayList<String> = Config.CONTENT_TITLES

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titleList[position]
    }

    fun setData(){
        fragmentList = ArrayList()
        for(title in titleList)
            fragmentList.add(ContentFragment.newInstance(title))
        notifyDataSetChanged()
    }
}