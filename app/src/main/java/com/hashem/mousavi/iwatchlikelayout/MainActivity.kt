package com.hashem.mousavi.iwatchlikelayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.hashem.mousavi.iwatchlikelayout.ui.CircularLayout
import com.hashem.mousavi.iwatchlikelayout.ui.theme.IWatchLikeLayoutTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getApplications()

        setContent {
            IWatchLikeLayoutTheme {

                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = R.drawable.wallpaper),
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )
                    CircularLayout(state = viewModel.state)
                }
            }
        }
    }
}
