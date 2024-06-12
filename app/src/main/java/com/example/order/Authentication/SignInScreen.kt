
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app.R
import com.example.order.AppTextField
import com.example.order.ButtonWithImage
import com.example.order.FilledButton
import com.example.order.Footer
import com.example.order.model.User
import com.example.order.ui.theme.OrderTheme
import com.example.order.ui.theme.blue
import com.example.order.ui.theme.metropolisFontFamily
import com.example.order.ui.theme.primaryFontColor
import com.example.order.ui.theme.red
import com.example.order.ui.theme.secondaryFontColor

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignInScreen(
    navToHome:(User) -> Unit,
    navToSignUp: () -> Unit,
    signInViewModel: SignInViewModel = viewModel()
) {
    val viewModel: SignInViewModel = viewModel()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val user by viewModel.user.observeAsState()


    OrderTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                text = "Đăng nhập",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontFamily = metropolisFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = primaryFontColor
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Điền thông tin của bạn để đăng nhập",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = secondaryFontColor,
                    fontWeight = FontWeight.Medium,
                    fontFamily = metropolisFontFamily
                )
            )
            Spacer(modifier = Modifier.height(36.dp))
            AppTextField(hint = "Email", keyboardType = KeyboardType.Email) { value ->
                email = value // Cập nhật biến email
            }
            Spacer(modifier = Modifier.height(28.dp))
            AppTextField(hint = "Mật khẩu", keyboardType = KeyboardType.Password) { value ->
                password = value // Cập nhật biến password
            }
            Spacer(modifier = Modifier.height(10.dp))
            errorMessage?.let { message ->
                Text(
                    text = message,
                    color = Color.Red,
                    fontSize = 15.sp,
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            FilledButton(
                text = "Đăng nhập",
                modifier = Modifier.padding(horizontal = 34.dp)
            ) {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    signInViewModel.signIn(
                        email = email,
                        password = password,
                        onSuccess = {
                            user?.let {
                                navToHome(it) // Điều hướng đến trang chủ và truyền thông tin người dùng
                            }
                        },
                        onError = { message ->
                            errorMessage = message
                        }

                    )
                } else {
                    errorMessage = "Vui lòng điền thông tin"
                }
            }

            Spacer(modifier = Modifier.height(25.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(Color.LightGray)
                )
                Text(
                    text = "Hoặc",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = secondaryFontColor,
                        fontFamily = metropolisFontFamily,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(Color.LightGray)
                )
            }
            Spacer(modifier = Modifier.height(28.dp))
            ButtonWithImage(
                text = "Đăng nhập bằng Facebook",
                image = R.drawable.ic_facebook,
                modifier = Modifier.padding(horizontal = 34.dp),
                color = blue
            ) {}
            Spacer(modifier = Modifier.height(28.dp))
            ButtonWithImage(
                text = "Đăng nhập bằng Google",
                image = R.drawable.ic_google,
                modifier = Modifier.padding(horizontal = 34.dp),
                color = red
            ) {}
            Spacer(modifier = Modifier.height(28.dp))
            Footer(
                text = "Bạn chưa có tài khoản?",
                textButton = "Đăng kí ngay",
            ) {
                navToSignUp()
            }
        }
    }
}


