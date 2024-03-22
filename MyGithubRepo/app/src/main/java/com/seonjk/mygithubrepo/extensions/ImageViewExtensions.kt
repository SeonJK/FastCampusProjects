package com.seonjk.mygithubrepo.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

internal fun ImageView.clear() = Glide.with(context).clear(this)

internal fun ImageView.loadCenterInside(url: String, corner: Float = 0f) = Glide.with(this)
    .load(url)
    .transition(DrawableTransitionOptions.withCrossFade(300))
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .apply {
        if (corner > 0) transform(CenterInside(), RoundedCorners(corner.fromDpToPx()))
    }
    .into(this)