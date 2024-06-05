package com.example.order.Authentication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.order.AppTextField
import com.example.order.FilledButton
import com.example.order.ui.theme.OrderTheme
import com.example.order.ui.theme.metropolisFontFamily
import com.example.order.ui.theme.primaryFontColor
import com.example.order.ui.theme.secondaryFontColor

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ResetPasswordScreen() {
    var email by remember { mutableStateOf("") }
    OrderTheme {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                text = "Quên mật khẩu",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontFamily = metropolisFontFamily,
                    color = primaryFontColor
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Vui lòng nhập email của bạn để nhận mã tạo mật khẩu mới qua email",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = secondaryFontColor,
                    fontFamily = metropolisFontFamily,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(horizontal = 60.dp)
            )
            Spacer(modifier = Modifier.height(60.dp))
            AppTextField(hint = "Email", keyboardType = KeyboardType.Email) { value ->
                email = value // Cập nhật biến email
            }
            Spacer(modifier = Modifier.height(34.dp))
            FilledButton(text = "Gửi", modifier = Modifier.padding(horizontal = 34.dp)) {


            }
        }
    }
}
@Composable
@Preview
fun resetPreview() {
    ResetPasswordScreen ()
}