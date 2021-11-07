package com.example.shortvideos

import android.app.Activity
import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ShortVideoCompose(
    activity: Activity,
    VideoItemsUrl:List<String>,
    ClickItemPosition:Int = 0,
    videoHeader:@Composable () -> Unit = {},
    videoBottom:@Composable () -> Unit = {}
) {
    //Translate StatusBar
    activity.immersive(darkMode = true)
    //开源库,当前页面选中状态
    val pagerState: PagerState = run {
        remember {
            PagerState(ClickItemPosition, 0, VideoItemsUrl.size - 1)
        }
    }
    //初始全局黑屏遮盖布局状态
    val initialLayout= remember {
        mutableStateOf(true)
    }
    //暂停图标是否显示主题
    val pauseIconVisibleState = remember {
        mutableStateOf(false)
    }
    //开源框架Pager,和框架ViewPagerChild.kt效果类似,使用它是为了结合button(下一页)使用
    Pager(
        state = pagerState,
        orientation = Orientation.Vertical,
        offscreenLimit = 1
    ) {
        //ViewPager单个页面,page变量在类PagerScope中返回回来的
        //滑动到的每一个Viewpager的item都隐藏一下暂停播放图标
        pauseIconVisibleState.value=false
        SingleVideoItemContent(VideoItemsUrl[page],
            pagerState,
            page,
            initialLayout,
            pauseIconVisibleState,
            videoHeader,
            videoBottom)
    }

    LaunchedEffect(ClickItemPosition){
        delay(300)
        initialLayout.value=false
    }

}

@Composable
private fun SingleVideoItemContent(
    videoUrl: String,
    pagerState: PagerState,
    pager: Int,
    initialLayout: MutableState<Boolean>,
    pauseIconVisibleState: MutableState<Boolean>,
    VideoHeader: @Composable() () -> Unit,
    VideoBottom: @Composable() () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()){
        //视频
        VideoPlayer(videoUrl,pagerState,pager,pauseIconVisibleState)
        //视频头部布局
        VideoHeader.invoke()
        //视频底部布局,注意:这里的Modifier.align必须有一个父组件,Column的父组件是Box
        Box(modifier = Modifier.align(Alignment.BottomStart)){
            //视频底部
            VideoBottom.invoke()
        }
        //初始黑屏遮盖布局
        if (initialLayout.value) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black))
        }
    }
}

@Composable
fun VideoPlayer(
    videoUrl: String,
    pagerState: PagerState,
    pager: Int,
    pauseIconVisibleState: MutableState<Boolean>,
) {
    val context = LocalContext.current
    val scope= rememberCoroutineScope()

    val exoPlayer = remember {
        SimpleExoPlayer.Builder(context)
            .build()
            .apply {
                val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
                    context,
                    Util.getUserAgent(context, context.packageName)
                )
                val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(videoUrl))

                this.prepare(source)
            }
    }
    //判断是否当前item,是就播放不然暂停
    if (pager == pagerState.currentPage) {
        exoPlayer.playWhenReady = true
        exoPlayer.play()
    } else {
        exoPlayer.pause()
    }
    exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
    exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

    DisposableEffect(
        Box(modifier = Modifier.fillMaxSize()){
            AndroidView(factory = {
                PlayerView(context).apply {
                    hideController()
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM

                    player = exoPlayer
                    layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                }
            },modifier = Modifier.noRippleClickable {
                pauseIconVisibleState.value=true
                exoPlayer.pause()
                scope.launch {
                    delay(500)
                    if (exoPlayer.isPlaying) {
                        exoPlayer.pause()
                    } else {
                        pauseIconVisibleState.value=false
                        exoPlayer.play()
                    }
                }
            })
            if (pauseIconVisibleState.value)
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(80.dp))
        }
    ) {
        onDispose {
            //退出时走这里
            exoPlayer.release()
        }
    }
}