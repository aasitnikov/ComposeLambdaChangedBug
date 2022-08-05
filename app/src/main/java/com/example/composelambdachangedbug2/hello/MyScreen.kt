package com.example.composelambdachangedbug2.hello

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Immutable
class Nothing {
    fun doNothing() = Unit
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyScreen() {
    val lazyListState = rememberLazyListState()

    val height = 60.dp
    val heightPx = with(LocalDensity.current) { height.roundToPx() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyListState,
    ) {
        items(100) {
            ListItem(Modifier.height(height)) {
                Text("Item #$it")
            }
        }
    }

    val nothing = remember { Nothing() }

    ShareRoundButton(
        onShareClick = nothing::doNothing,
        alpha = { lazyListState.firstVisibleItemScrollOffset.toFloat() / heightPx },
    )
}

@Composable
fun RoundButtonWithText(
    modifier: Modifier = Modifier,
    button: @Composable () -> Unit,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        button()
    }
}

@Composable
fun ShareRoundButton(
    onShareClick: () -> Unit,
    alpha: () -> Float,
) {
    // This call is crucial for bug to appear
    RoundButtonWithText(
        button = {
            println("ShareRoundButton: recomposing because alpha() changed")
            SecondaryRoundButton(
                modifier = Modifier,
                onClick = onShareClick,
                clickEnabled = alpha() > 0.5f,
            ) {
                Icon(
                    Icons.Default.Share,
                    contentDescription = null,
                )
            }
        },
    )
}

@Composable
fun SecondaryRoundButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    clickEnabled: Boolean = true,
    rippleColor: Color = Color.Yellow,
    content: @Composable BoxScope.() -> Unit
) {
    // Should recompose only when clickEnabled changes
    onClick; enabled; clickEnabled; rippleColor;
    Box(modifier) {
        content()
        TertiaryRoundButton(onClick = onClick)
    }
}

@Composable
fun TertiaryRoundButton(onClick: () -> Unit) {
    println("Why am I even recomposing? My lambda is ${System.identityHashCode(onClick)}")
    Box(Modifier.clickable(onClick = onClick))
}
