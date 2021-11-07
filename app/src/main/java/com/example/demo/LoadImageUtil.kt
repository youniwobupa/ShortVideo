package com.example.demo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

object LoadImageUtil {

    fun initImageWithState(
        context: Context, url: String,
        @DrawableRes defaultImageId: Int = R.drawable.backgroundwhite
    ): MutableState<Bitmap?> {

        val TAG = "LoadImage"
        val bitmapState: MutableState<Bitmap?> = mutableStateOf(null)
        //如果图片路径为空
        if (url.trim().isEmpty()) {
            //先加载本地图片,并保持到bitmapState状态中
            Glide.with(context)
                .asBitmap()
                .load(defaultImageId)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        //自定义Target，在加载完成后将图片资源传递给bitmapState
                        bitmapState.value = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })

        } else { //不然如果图片路径不为空

            //为请求加上 Headers ，提高访问成功率
            val glideUrl = GlideUrl(
                url,
                LazyHeaders.Builder().addHeader(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36 Edg/91.0.864.67"
                ).build()
            )

            //先加载本地图片,并保持到bitmapState状态中
            Glide.with(context)
                .asBitmap()
                .load(defaultImageId)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        //自定义Target，在加载完成后将图片资源传递给bitmapState
                        bitmapState.value = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })

            //然后再加载网络图片,并保持到bitmapState状态中
            try {
                Glide.with(context)
                    .asBitmap()
                    .load(glideUrl)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            //自定义Target，在加载完成后将图片资源传递给bitmapState
                            bitmapState.value = resource
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })
            } catch (glideException: GlideException) {
                Log.d(TAG, "loadImage: ${glideException.rootCauses}")
            }
        }

        //返回bitmapState图片状态
        return bitmapState
    }

}