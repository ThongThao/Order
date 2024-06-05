package com.example.admin.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.admin.R
import com.example.admin.viewmodel.LogInViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginAdmin(
    context:Context,
    logInViewModel: LogInViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.img), // Đặt đường dẫn đến ảnh nền tại đây
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Box(
            modifier = Modifier
                .height(370.dp)
                .width(300.dp)
                .align(Alignment.Center)
                .border(width = 2.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp))
        )
        {
            Column(
                modifier = Modifier
                    .height(350.dp)
                    .width(300.dp)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                Text(
                    text = "Administration ",
                    style = MaterialTheme.typography.bodyLarge
                        .copy(
                            fontSize = 30.sp,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Medium
                        )
                )
                Text(
                    text = "for OrderNow",
                    style = MaterialTheme.typography.bodyLarge
                        .copy(
                            fontSize = 30.sp,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF7389FF)
                        ),

                    )
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email", color = Color(0xFF7389FF)) },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF7389FF),
                        unfocusedBorderColor = Color(0xFF7389FF)
                    ),
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Mật khẩu",color = Color(0xFF7389FF)) },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    visualTransformation = if (!passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF7389FF),
                        unfocusedBorderColor = Color(0xFF7389FF)
                    ),
                    trailingIcon = {
                        IconButton(
                            onClick = { passwordVisibility = !passwordVisibility },
                        ) {
                            Icon(
                                painter = painterResource(if (!passwordVisibility) R.drawable.off else R.drawable.on),
                                contentDescription = if (passwordVisibility) "Hide password" else "Show password"
                            )
                        }
                    }

                )
                Spacer(modifier = Modifier.height(7.dp))
                errorMessage?.let { message ->
                    Text(
                        text = message,
                        color = Color.Red,
                        fontSize = 15.sp,
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (email.isNotEmpty() && password.isNotEmpty()) {
                            logInViewModel.signIn(
                                email = email,
                                password = password,
                                onSuccess = {
                                    context.startActivity(Intent(context, Home::class.java))
                                },
                                onError = { message ->
                                    errorMessage = message
                                }

                            )
                        } else {
                            errorMessage = "Vui lòng điền thông tin"
                        } },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7389FF)),
                    modifier = Modifier.width(200.dp)
                ) {
                    Text("Đăng nhập", fontSize = 24.sp)
                }

            }
        }

    }
}
