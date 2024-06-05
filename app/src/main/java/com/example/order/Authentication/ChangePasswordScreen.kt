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
import androidx.compose.ui.text.input.ImeAction
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
fun ChangePasswordScreen() {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    OrderTheme {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                text = "Đặt lại mật khẩu",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontFamily = metropolisFontFamily,
                    color = primaryFontColor
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier = Modifier.padding(horizontal = 60.dp),
                text = "Vui lòng điền thông tin để đặt lại mật khẩu mới",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = secondaryFontColor,
                    fontFamily = metropolisFontFamily,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(36.dp))
            AppTextField(hint = "Mật khẩu", keyboardType = KeyboardType.Password) { value ->
                password = value // Cập nhật biến password
            }
            Spacer(modifier = Modifier.height(28.dp))
            AppTextField(
                hint = "Nhập lại mật khẩu",
                keyboardType = KeyboardType.Password,
                action = ImeAction.Done
            ) { value ->
                confirmPassword = value // Cập nhật biến confirmPassword
            }

            Spacer(modifier = Modifier.height(28.dp))
            FilledButton(text = "Tiếp", modifier = Modifier.padding(horizontal = 34.dp)) {}
        }
    }
}
@Composable
@Preview
fun changePreview() {
    ChangePasswordScreen()
}