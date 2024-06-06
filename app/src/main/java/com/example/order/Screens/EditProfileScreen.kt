
import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.app.R
import com.example.order.Screens.ProfileImage
import com.example.order.ui.theme.blue3
import com.example.order.ui.theme.orange
import com.example.order.viewmodels.UserViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(userId: String?, userViewModel: UserViewModel = viewModel(), navController: NavHostController) {
    val user by userViewModel.getUser(userId).observeAsState()
    user?.let {
        var name by remember { mutableStateOf(user!!.fullName) }
        var address by remember { mutableStateOf(user?.address ?: "") }
        var phone by remember { mutableStateOf(user!!.phoneNumber) }
        var birthdayString by remember { mutableStateOf(user?.birthday?.toString() ?: "") }
        var isFocused by remember { mutableStateOf(false) }
        val calendar = Calendar.getInstance()
        val context = LocalContext.current
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()){

                    IconButton(
                        onClick = {  navController.popBackStack() }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = null,
                            tint = orange,
                            modifier = Modifier.size(24.dp)
                        )

                    }
                    Spacer(modifier = Modifier.width(58.dp))
                    androidx.compose.material3.Text(
                        text = "Thông tin cá nhân",
                        fontSize = 24.sp,
                        fontWeight = MaterialTheme.typography.h6.fontWeight,
                    )

                }
               Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                ) {
                    Image(
                        painter = painterResource(id =  R.drawable.town),
                        contentDescription ="" ,
                        contentScale = ContentScale.Crop,
                    )
                }

            }
        Column(
            modifier = Modifier
                .padding(top = 50.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)
        ) {
            user?.image.let {
                ProfileImage(image = it.toString())
            }
            name?.let {
                OutlinedTextField(
                    value = it,
                    onValueChange = { name = it },
                    label = {
                        Text(
                            "Họ và tên",
                            color = if (isFocused) orange else Color.Gray,
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                        },
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = orange,
                        focusedIndicatorColor = blue3,
                        backgroundColor = Color.White

                    ),
                )
            }
            phone?.let {
                OutlinedTextField(
                    value = it,
                    onValueChange = { phone = it },
                    label = {
                        Text(
                            "Số điện thoại",
                            color = if (isFocused) orange else Color.Gray,
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                        },
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = orange,
                        focusedIndicatorColor = blue3,
                        backgroundColor = Color.White

                    ),
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = birthdayString,
                    onValueChange = { birthdayString = it },
                    label = {
                        Text(
                            "Ngày sinh",
                            color = if (isFocused) orange else Color.Gray,
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                        },
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = orange,
                        focusedIndicatorColor = blue3,
                        backgroundColor = Color.White
                    ),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                showDatePickerDialog(
                                    context,
                                    calendar
                                ) { year, month, day ->
                                    val formattedBirthday = "$day/${month + 1}/$year"
                                    birthdayString = formattedBirthday
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Pick Date",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                )
            }
            address?.let {
                OutlinedTextField(
                    value = it,
                    onValueChange = { address = it },
                    label = {
                        Text(
                            "Địa chỉ",
                            color = if (isFocused) orange else Color.Gray,
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                        },
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = orange,
                        focusedIndicatorColor = blue3,
                        backgroundColor = Color.White

                    ),
                )
             }
            val updatedBirthday: Date? = try {
                // Parse the birthdayString back to a Date object
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(birthdayString)
            } catch (e: ParseException) {
                null
            }
            Button(onClick = {

                user?.let {
                    val updatedUser = it.copy(
                        fullName = name,
                        phoneNumber = phone,
                        address = address,
                        birthday = birthdayString
                    )
                    userViewModel.updateUser(userId, updatedUser,
                        onSuccess = {
                            // Handle success, maybe navigate back
                            navController.popBackStack()
                        },
                        onFailure = { e ->
                            // Handle failure, maybe show an error message
                        }
                    )
                }
            }) {
                Text(text = "Lưu")
            }


        }

        }
    }
fun showDatePickerDialog(context: Context, calendar: Calendar, onDateSelected: (Int, Int, Int) -> Unit) {
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            onDateSelected(selectedYear, selectedMonth, selectedDay)
        },
        year,
        month,
        day
    ).show()
}
