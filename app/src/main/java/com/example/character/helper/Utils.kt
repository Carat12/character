package com.example.character.helper

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(msg: String){
    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
}

fun Activity.toast(msg: String){
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}