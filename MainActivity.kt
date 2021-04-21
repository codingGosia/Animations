package com.gosia.animations

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gosia.animations.ui.theme.AnimationsTheme

const val TAG:String = "AppDebug"

@MainActivity.AnimationsTheme
class MainActivity : ComponentActivity() {
    annotation class AnimationsTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val animationState = remember{ mutableStateOf(false)}
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.LightGray)
            ) {
                Fly(
                    isFlyEnabled = animationState.value,
                    maxWidth = maxWidth,
                    maxHeight = maxHeight,
                )

                LaunchButton(
                    animationState = animationState.value,
                    onToggleAnimationState = { animationState.value = !animationState.value }
                )
              }
        }
    }
}
@Composable
fun Fly(
    isFlyEnabled: Boolean,
    maxWidth: Dp,
    maxHeight: Dp,
) {
    val resource: Painter
    val modifier: Modifier
    val flySize = 200.dp

    if(!isFlyEnabled){
        resource = painterResource(id = R.drawable.fly1)
        modifier = Modifier.offset(
            y = maxHeight - flySize,
        )
    }
    else{
        val infiniteTransition = rememberInfiniteTransition()
        val engineState = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 500,
                    easing = LinearEasing
                )
            )
        )
        val xPositionState = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 2000,
                    easing = LinearEasing
                )
            )
        )
        if (engineState.value <= .5f) {
            resource = painterResource(id = R.drawable.fly2)
        } else {
            resource = painterResource(id = R.drawable.fly3)
        }
        modifier = Modifier.offset(
            x = (maxWidth - flySize) * xPositionState.value,
            y = (maxHeight - flySize) - (maxHeight - flySize) * xPositionState.value,
        )
    }
    Image(
        modifier = modifier.width(flySize).height(flySize),
        painter = resource,
        contentDescription = "Fly",
    )
}


@Composable
fun LaunchButton(
    animationState: Boolean,
    onToggleAnimationState: () -> Unit,
){
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ){
        if(animationState){
            Button(
                onClick = onToggleAnimationState,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray, contentColor = Color.White)
            ) {
                Text("STOP")
            }
        }
        else{
            Button(
                onClick = onToggleAnimationState,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Cyan, contentColor = Color.White)
            ) {
                Text("FLY!")
            }
        }
    }
}
