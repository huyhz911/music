package com.example.music

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter

/**
 * Created by Bkav HuyNgQe on 08/07/2022.
 */
@BindingAdapter("bitMap", "resources", requireAll = true)
fun setBackGround(view: View, bitMap: Bitmap, resources: Resources){
    view.background = BitmapDrawable(resources, bitMap)
}
