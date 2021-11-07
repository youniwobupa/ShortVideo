package com.example.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.demo.ui.theme.DemoTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting(this)
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun Greeting(activity: MainActivity) {
    VideoCompose(activity)
}
@ExperimentalFoundationApi
@Composable
fun VideoCompose(activity: MainActivity) {
    //第三方库垂直交错布局
    LazyVerticalGrid(cells = GridCells.Fixed(2)) {
        itemsIndexed(videos) { index,video ->
            SingleVideoItem(index,video,activity)
        }
    }
}

@Composable
fun SingleVideoItem(index: Int, video: Video, activity: MainActivity) {
    //返回随机高度,注意:这里remember函数使用了参数
    val itemRandomHeight = remember(index) { Random.nextInt(280, 360).dp }
    Card(
        elevation = 10.dp, modifier = Modifier
            .padding(8.dp)
            .clickable {

            val intent=VideoActivity.instancesIntent(activity,index)
                activity.startActivity(intent)


            }, border = BorderStroke(1.5.dp, MaterialTheme.colors.primaryVariant)
    ) {
        Column {
            BitmapCompose(envelopePic = video.pic, itemRandomHeight = itemRandomHeight)
            Text(
                text = "短视频${index}",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.h6.copy(fontSize = 18.sp)
            )
        }
    }
}

@Composable
private fun BitmapCompose(envelopePic: String, itemRandomHeight: Dp) {
    //获取上下文
    val context = LocalContext.current
    //通过LoadImageUtil工具类获取图片状态: 默认图片还是Glide加载网络图片后的状态
    val imagestate = LoadImageUtil.initImageWithState(context, envelopePic)
    //从状态中取出图片资源,并通过let表达式显示图片
    imagestate.value?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(itemRandomHeight),
        )
    }
}

data class Video(val position: Int, val pic: String, val mp4Url: String)
val videos = mutableStateListOf<Video>(
    Video(
        position = 0,
        pic = "https://mvimg10.meitudata.com/618523cac04c4nmco4ti0b1280.jpg!feed320", mp4Url =
        "https://mvvideo10.meitudata.com/618523cb0c2f3ki3frxfpz3345.mp4"
    ),
    Video(
        position = 1, pic = "https://mvimg10.meitudata.com/617d71c6b6e8419kmczmh51824.jpg!feed320",
        mp4Url = "https://mvvideo10.meitudata.com/617d71c68a23b07s660jli8219.mp4"
    ),
    Video(
        position = 2, pic = "https://mvimg10.meitudata.com/617e0891c81c0872ukbagv4608.jpg!feed320",
        mp4Url = "https://mvvideo10.meitudata.com/617e0891ad406q8may4hsy4298.mp4"
    )
)
val videoUrls= listOf("https://mvvideo10.meitudata.com/618523cb0c2f3ki3frxfpz3345.mp4",
    "https://mvvideo10.meitudata.com/617d71c68a23b07s660jli8219.mp4",
    "https://mvvideo10.meitudata.com/617e0891ad406q8may4hsy4298.mp4",)