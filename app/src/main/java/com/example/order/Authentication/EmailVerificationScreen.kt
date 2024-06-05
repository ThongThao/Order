package com.example.order.Authentication

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.order.FilledButton
import com.example.order.Footer
import com.example.order.VerifyTextField
import com.example.order.ui.theme.OrderTheme
import com.example.order.ui.theme.metropolisFontFamily
import com.example.order.ui.theme.primaryFontColor
import com.example.order.ui.theme.secondaryFontColor
import com.example.order.viewmodels.ResetPasswordViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun EmailVerificationScreen() {
    val resetPasswordViewModel = viewModel<ResetPasswordViewModel>()
//    val snackbarHostState = remember { SnackbarHostState() }
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    OrderTheme {
        Scaffold(scaffoldState = scaffoldState) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(60.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 50.dp),
                    text = "Chúng tôi đã gửi mã tới email của bạn",
                    style = TextStyle(
                        fontSize = 25.sp,
                        fontFamily = metropolisFontFamily,
                        color = primaryFontColor,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Vui lòng kiểm tra email của bạn để tiếp tục thiết lập lại mật khẩu ",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = secondaryFontColor,
                        fontFamily = metropolisFontFamily,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(horizontal = 50.dp)
                )
                Spacer(modifier = Modifier.height(54.dp))
                VerifyTextField(viewModel = resetPasswordViewModel)
                Spacer(modifier = Modifier.height(36.dp))
                FilledButton(text = "Tiếp", modifier = Modifier.padding(horizontal = 34.dp)) {
                    if (resetPasswordViewModel.checkVerificationCode())

                    else {
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("Error: Invalid Code", actionLabel = "Try Again")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Footer(text = "Bạn chưa nhận được mã?", textButton = "Bấm vào đây") {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar("Mã xác minh là: " + resetPasswordViewModel.getVC(), actionLabel = "OK")
                    }
                }
            }
        }
    }
}
@Composable
@Preview
fun emailPreview() {
    EmailVerificationScreen()
}