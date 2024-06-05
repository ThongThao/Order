package com.example.order.Authentication

import SignUpViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.order.AppTextField
import com.example.order.FilledButton
import com.example.order.Footer
import com.example.order.ui.theme.OrderTheme
import com.example.order.ui.theme.gray
import com.example.order.ui.theme.metropolisFontFamily
import com.example.order.ui.theme.primaryFontColor
import com.example.order.ui.theme.secondaryFontColor
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignUpScreen(
    navToLogin: () -> Unit,
    signUpViewModel: SignUpViewModel = viewModel() // Inject SignUpViewModel
) {
    val auth = FirebaseAuth.getInstance()
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var gender by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("Nam", "Nữ", "Khác")

    OrderTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                text = "Đăng kí",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontFamily = metropolisFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = primaryFontColor
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Điền thông tin của bạn để đăng kí",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = metropolisFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = secondaryFontColor,
                )
            )

            Spacer(modifier = Modifier.height(36.dp))
            AppTextField(hint = "Họ và Tên") { value ->
                name = value
            }
            Spacer(modifier = Modifier.height(28.dp))
            AppTextField(hint = "Số điện thoại") { value ->
                phone = value
            }
            Spacer(modifier = Modifier.height(28.dp))
            Column(
                modifier = Modifier.width(350.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.size(7.dp))
                Surface(
                    shape = RoundedCornerShape(28.dp),

                    color = gray,
                    modifier = Modifier.height(58.dp).fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = gender.takeIf { it.isNotBlank() } ?: "Chọn giới tính",
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 15.dp),

                            style = TextStyle(color = Color.Black),
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        )
                        IconButton(
                            onClick = { expanded = true },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Dropdown Icon"
                            )
                        }
                    }
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.width(150.dp)
                ) {
                    genderOptions.forEach { option ->
                        DropdownMenuItem(
                            onClick = {
                                gender = option
                                expanded = false
                            }
                        ) {
                            Text(
                                text = option,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(28.dp))
            AppTextField(hint = "Email", keyboardType = KeyboardType.Email) { value ->
                email = value // Cập nhật biến email
            }
            Spacer(modifier = Modifier.height(28.dp))
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



            // Error message if passwords don't match
            errorMessage?.let { message ->
                Text(
                    text = message,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
            FilledButton(text = "Đăng kí", modifier = Modifier.padding(horizontal = 34.dp)) {
                if (password == confirmPassword) {
                    signUpViewModel.signUp(
                        email = email,
                        password = password,
                        fullName = name,
                        phoneNumber = phone,
                        gender = gender,
                        onSuccess = {
                            navToLogin()
                        },
                        onError = { message ->
                            errorMessage = message
                        }
                    )
                } else {
                    errorMessage = "Mật khẩu không khớp."
                }
            }


            Spacer(modifier = Modifier.height(28.dp))
            Footer(
                text = "Bạn đã có tài khoản?",
                textButton = "Đăng nhập ngay",
            ) {
                navToLogin()
            }
        }

    }
}


