package com.hashem.mousavi.iwatchlikelayout.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.hashem.mousavi.iwatchlikelayout.MyData
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun CircularLayout(
    state: State<List<MyData>>,
    minScale: Float = 0.3f
) {
    val maxItemInRow = 5

    var middleX by remember {
        mutableStateOf(1f)
    }

    var middleY by remember {
        mutableStateOf(1f)
    }

    var width by remember {
        mutableStateOf(1)
    }

    var height by remember {
        mutableStateOf(1)
    }


    val xyList = remember {
        mutableStateListOf<Pair<Int, Int>>().apply {
            repeat(state.value.size) {
                add(Pair(0, 0))
            }
        }
    }

    var itemSize by remember {
        mutableStateOf(0.dp)
    }

    var offset by remember {
        mutableStateOf(Offset.Zero)
    }

    val density = LocalDensity.current

    Layout(
        content = {
            state.value.forEachIndexed { index, myData ->
                Item(
                    modifier = Modifier
                        .offset {
                            IntOffset(x = offset.x.toInt(), y = offset.y.toInt())
                        },
                    myData = myData,
                    size = itemSize,
                    scale = xyList[index].let { pair ->
                        val distance = sqrt(
                            (pair.first + offset.x - middleX).pow(2) + (pair.second + offset.y - middleY).pow(
                                2
                            )
                        )
                        (1 - 2 * distance * (1 - minScale) / height).coerceAtLeast(minScale)
                    }
                )
            }
        },
        modifier = Modifier
            .onSizeChanged {
                itemSize = with(density) { it.width.toDp() } / maxItemInRow
            }
            .pointerInput(true) {
                detectDragGestures { _, dragAmount ->
                    offset += dragAmount
                }
            }
    ) { measurables, constraints ->

        val placeables =
            measurables.map { it.measure(constraints.copy(minWidth = 0, minHeight = 0)) }

        width = constraints.maxWidth
        height = constraints.maxHeight

        layout(width = width, height = height) {

            middleX = (width / 2).toFloat()
            middleY = (height / 2).toFloat()

            val size = placeables.firstOrNull()?.width ?: 0

            var x = 0
            var y = (size / 2)
            var row = 0
            var toggle = true

            placeables.forEachIndexed { index, placeable ->
                xyList[index] = Pair(x + size / 2, y + size / 2)
                placeable.place(x, y)

                x += size
                if (toggle) {
                    if (x + size > width) {
                        x = size / 2
                        toggle = false
                        row++
                    }
                } else {
                    if (x + 3 * size / 2 > width) {
                        x = 0
                        toggle = true
                        row++
                    }
                }

                y = (size / 2 + size * row)
            }

        }

    }
}

@Composable
fun Item(
    modifier: Modifier,
    myData: MyData,
    size: Dp,
    scale: Float
) {
    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .size(size),
            bitmap = myData.drawable.toBitmap().asImageBitmap(),
            contentDescription = "",
            contentScale = ContentScale.Fit,

        )
    }
}