package com.example.order.Welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.R
import com.example.order.BorderButton
import com.example.order.FilledButton
import com.example.order.Logo
import com.example.order.ui.theme.OrderTheme
import com.example.order.ui.theme.metropolisFontFamily
import com.example.order.ui.theme.secondaryFontColor

@Composable
fun WelcomeScreen(navToLogin: () -> Unit,navToSignup: () -> Unit) {
    OrderTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopSection()
            Spacer(modifier = Modifier.size(35.dp))
            Text(
                text = "Khám phá những món ăn ngon nhất từ các nhà hàng và giao hàng nhanh chóng đến tận nhà bạn",
                style = TextStyle(
                    color = secondaryFontColor,
                    fontFamily = metropolisFontFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(horizontal = 40.dp)
            )
            FilledButton(
                modifier = Modifier
                    .padding(horizontal = 34.dp)
                    .padding(top = 56.dp),
                text = "Đăng nhập"
            ) {
                navToLogin()
            }
            BorderButton(
                modifier = Modifier
                    .padding(horizontal = 34.dp, vertical = 24.dp),
                text = "Tạo tài khoản",
            ) {
                navToSignup()
            }
        }
    }
}

@Composable
private fun TopSection() {
    Box {
        Image(
            painter = painterResource(id = R.drawable.ic_sign_in_top),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 95.dp),
            contentScale = ContentScale.FillWidth,
        )
        Logo(modifier = Modifier.align(Alignment.BottomCenter).padding(top = 280.dp))
    }

}
