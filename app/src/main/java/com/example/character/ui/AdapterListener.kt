package com.example.character.ui

import android.view.View

interface AdapterListener {
    fun onItemClicked(view: View, position: Int)
}