package com.example.demo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.example.shortvideos.ShortVideoCompose


class VideoActivity : ComponentActivity() {
    val position by lazy { intent.getIntExtra(POS,0) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyVideoCompose(position,this)
        }
    }

    companion object {
        val POS="positon"
        fun instancesIntent(context: Context,position:Int) =Intent().apply {
            putExtra(POS,position)
            setClass(context,VideoActivity::class.java)
        }
    }
}

@Composable
fun MyVideoCompose(position: Int, videoActivity: VideoActivity) {
    ShortVideoCompose(activity = videoActivity,
        VideoItemsUrl = videoUrls,
        ClickItemPosition = position
    )
      //Jetpack Compose short video
//    ShortVideoCompose(activity = videoActivity,
//        VideoItemsUrl = videoUrls,
//        ClickItemPosition = position,
//        videoHeader = {
//            VideosHeader()
//        },
//        videoBottom = {
//            VideoBottom()
//        }
//    )
}




@Composable
private fun VideoBottom() {
    Column {
        Text("底部布局1", color = Color.White, modifier = Modifier.padding(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text("底部布局2", color = Color.White, modifier = Modifier.padding(horizontal = 10.dp))
            Text("底部布局3", color = Color.White, modifier = Modifier.padding(horizontal = 10.dp))
        }
    }
}

@Composable
private fun VideosHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Video", color = Color.White)
        Icon(
            ImageBitmap.imageResource(id = R.drawable.ic_outlined_camera),
            tint = Color.White,
            modifier = Modifier.size(30.dp),
            contentDescription = ""
        )
    }
}