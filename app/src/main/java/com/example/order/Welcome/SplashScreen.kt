package com.example.order.Welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.order.BorderButton
import com.example.order.Logo
import com.example.app.R
import com.example.order.ui.theme.OrderTheme

@Composable
fun SplashScreen(navToPageview: () -> Unit) {
    OrderTheme {
        Box {
            Image(
                painter = painterResource(id = R.drawable.ic_splash_background),
                contentDescription = null,
                Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Logo(modifier = Modifier.align(Alignment.Center))
            BorderButton(
                modifier = Modifier.align(Alignment.Center)
                    .padding(horizontal = 100.dp, vertical = 24.dp)
                    .padding(top = 300.dp),
                text = "Bắt đầu",
            ) {
                  navToPageview()
            }
        }


    }
}
